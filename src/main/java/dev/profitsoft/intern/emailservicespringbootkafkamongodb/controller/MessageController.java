package dev.profitsoft.intern.emailservicespringbootkafkamongodb.controller;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.dto.MessageSaveDto;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class MessageController {

    @Value("${kafka.topic.messageReceived}")
    private String messageReceivedTopic;

    private final KafkaOperations<String, ReceivedMessage> kafkaOperations;

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<String> receiveEmail(@RequestBody MessageSaveDto dto) {
        kafkaOperations.send(
                messageReceivedTopic,
                dto.getSubject(),
                messageService.toMessage(dto)
        );

        return ResponseEntity.ok("Email received successfully");
    }

}
