package dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.impl;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.dto.MessageSaveDto;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.ErrorDeliver;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.MessageStatus;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.repository.MessageRepository;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message saveMessage(ReceivedMessage receivedMessage) {
        Message message = new Message();
        message.setSubject(receivedMessage.getSubject());
        message.setContent(receivedMessage.getContent());
        message.setEmailsTo(receivedMessage.getEmailsTo());
        message.setStatus(MessageStatus.NEW);

        return messageRepository.save(message);
    }

    @Override
    public void updateMessageAfterSending(Message message, String transactionId, MessageStatus status) {
        setTransactionIdStatus(message, transactionId, status);
        messageRepository.save(message);
    }

    @Override
    public void updateMessageAfterSending(Message message, String transactionId, MessageStatus status, ErrorDeliver errorDeliver) {
        setTransactionIdStatus(message, transactionId, status);
        message.setLastErrorDeliver(errorDeliver);
        messageRepository.save(message);
    }

    @Override
    public boolean isPresentWithTransactionId(String transactionId) {
        return messageRepository.findByTransactionIdsIn(transactionId).isPresent();
    }

    @Override
    public List<Message> findAllByMessageStatus(MessageStatus status) {
        return messageRepository.findAllByStatus(status);
    }

    @Override
    public ReceivedMessage toMessage(MessageSaveDto dto) {
        return ReceivedMessage.builder()
                .subject(dto.getSubject())
                .content(dto.getContent())
                .emailsTo(dto.getEmailsTo())
                .transactionId(UUID.randomUUID().toString())
                .build();
    }

    private static void setTransactionIdStatus(Message message, String transactionId, MessageStatus status) {
        message.addTransactionId(transactionId);
        message.setStatus(status);
    }

}
