package dev.profitsoft.intern.emailservicespringbootkafkamongodb.service;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;

public interface EmailService {

    void processMessageReceived(ReceivedMessage message);

    void sendMessage(Message message, String transactionId);

}
