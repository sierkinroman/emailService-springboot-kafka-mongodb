package dev.profitsoft.intern.emailservicespringbootkafkamongodb.schedule;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.MessageStatus;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.EmailService;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageResender {

    private final MessageService messageService;

    private final EmailService emailService;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 5)
    public void resendMessage() {
        List<Message> errorDeliverMessages = messageService.findAllByMessageStatus(MessageStatus.ERROR_DELIVER);

        errorDeliverMessages.forEach(message ->
                emailService.sendMessage(message, UUID.randomUUID().toString()));
    }

}
