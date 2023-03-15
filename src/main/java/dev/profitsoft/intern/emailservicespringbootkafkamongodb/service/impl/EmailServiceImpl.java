package dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.impl;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.ErrorDeliver;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.MessageStatus;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.EmailService;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final MessageService messageService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void processMessageReceived(ReceivedMessage receivedMessage) {
        String transactionId = receivedMessage.getTransactionId();
        if (!messageService.isPresentWithTransactionId(transactionId)) {
            Message message = messageService.saveMessage(receivedMessage);
            sendMessage(message, transactionId);
        }
    }

    @Override
    public void sendMessage(Message message, String transactionId) {
        SimpleMailMessage mailMessage = prepareMailMessage(message);

        try {
            emailSender.send(mailMessage);
            messageService.updateMessageAfterSending(message, transactionId, MessageStatus.DELIVERED);
        } catch (MailException e) {
            messageService.updateMessageAfterSending(
                    message,
                    transactionId,
                    MessageStatus.ERROR_DELIVER,
                    new ErrorDeliver(
                            e.getClass().getName(),
                            e.getMessage()
                    )
            );
        }
    }

    private SimpleMailMessage prepareMailMessage(Message message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getContent());
        mailMessage.setTo(message.getEmailsTo().toArray(new String[0]));
        return mailMessage;
    }

}
