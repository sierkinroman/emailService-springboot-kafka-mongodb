package dev.profitsoft.intern.emailservicespringbootkafkamongodb.listener;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageReceivedListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.messageReceived}")
    public void emailReceived(ReceivedMessage message) {
        emailService.processMessageReceived(message);
    }

}
