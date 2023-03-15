package dev.profitsoft.intern.emailservicespringbootkafkamongodb.service;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.dto.MessageSaveDto;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.ErrorDeliver;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.MessageStatus;

import java.util.List;

public interface MessageService {

    Message saveMessage(ReceivedMessage message);

    void updateMessageAfterSending(Message message, String transactionId, MessageStatus status);

    void updateMessageAfterSending(Message message, String transactionId, MessageStatus status, ErrorDeliver errorDeliver);

    boolean isPresentWithTransactionId(String transactionId);

    List<Message> findAllByMessageStatus(MessageStatus status);

    ReceivedMessage toMessage(MessageSaveDto dto);

}
