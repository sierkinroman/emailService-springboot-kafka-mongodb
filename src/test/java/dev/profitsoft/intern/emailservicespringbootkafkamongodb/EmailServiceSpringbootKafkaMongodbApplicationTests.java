package dev.profitsoft.intern.emailservicespringbootkafkamongodb;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging.ReceivedMessage;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.MessageStatus;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.EmailService;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:19092", "port=19092" })
class EmailServiceSpringbootKafkaMongodbApplicationTests {

    @Value("${kafka.topic.messageReceived}")
    private String messageReceivedTopic;

    @Autowired
    private KafkaOperations<String, ReceivedMessage> kafkaOperations;

    @SpyBean
    private EmailService emailService;

    @MockBean
    private JavaMailSender emailSender;

    @Autowired
    private MessageService messageService;

    @Test
    public void testSuccessfullySendEmail() {
        ReceivedMessage message = ReceivedMessage.builder()
                .subject("subject")
                .content("content")
                .emailsTo(Set.of("sierkinroman@gmail.com"))
                .transactionId(UUID.randomUUID().toString())
                .build();

        doNothing()
                .when(emailSender).send(ArgumentMatchers.any(SimpleMailMessage.class));

        kafkaOperations.send(messageReceivedTopic, message.getSubject(), message);

        verify(emailService, after(5000)).processMessageReceived(any());

        List<Message> deliveredMessages = messageService.findAllByMessageStatus(MessageStatus.DELIVERED);
        assertThat(deliveredMessages).hasSize(1);
        assertThat(deliveredMessages.get(0).getSubject()).isEqualTo(message.getSubject());
        assertThat(deliveredMessages.get(0).getContent()).isEqualTo(message.getContent());
        assertThat(deliveredMessages.get(0).getEmailsTo()).isEqualTo(message.getEmailsTo());
    }

    @Test
    public void testFailedSendEmail() {
        ReceivedMessage message = ReceivedMessage.builder()
                .subject("subject")
                .content("content")
                .emailsTo(Set.of("sierkinroman@gmail.com"))
                .transactionId(UUID.randomUUID().toString())
                .build();

        String exceptionMessage = "Error while sending email";
        doThrow(new MailSendException(exceptionMessage))
                .when(emailSender).send(ArgumentMatchers.any(SimpleMailMessage.class));

        kafkaOperations.send(messageReceivedTopic, message.getSubject(), message);

        verify(emailService, after(5000)).processMessageReceived(any());

        List<Message> deliveredMessages = messageService.findAllByMessageStatus(MessageStatus.ERROR_DELIVER);
        assertThat(deliveredMessages).hasSize(1);
        assertThat(deliveredMessages.get(0).getSubject()).isEqualTo(message.getSubject());
        assertThat(deliveredMessages.get(0).getContent()).isEqualTo(message.getContent());
        assertThat(deliveredMessages.get(0).getEmailsTo()).isEqualTo(message.getEmailsTo());
        assertThat(deliveredMessages.get(0).getLastErrorDeliver().getExceptionClass()).isEqualTo(MailSendException.class.getName());
        assertThat(deliveredMessages.get(0).getLastErrorDeliver().getExceptionMessage()).isEqualTo(exceptionMessage);
    }

}
