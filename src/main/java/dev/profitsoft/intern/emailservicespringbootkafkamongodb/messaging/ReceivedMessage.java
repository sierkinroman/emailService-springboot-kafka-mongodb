package dev.profitsoft.intern.emailservicespringbootkafkamongodb.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Builder
@Jacksonized
public class ReceivedMessage {

    private String transactionId;

    private String subject;

    private String content;

    private Set<String> emailsTo;

}
