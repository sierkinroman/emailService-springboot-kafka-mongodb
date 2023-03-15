package dev.profitsoft.intern.emailservicespringbootkafkamongodb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Document("messages")
public class Message {

    @Id
    private String id;

    private String subject;

    private String content;

    private Set<String> emailsTo;

    private MessageStatus status;

    private List<String> transactionIds;

    private ErrorDeliver lastErrorDeliver;

    public void addTransactionId(String transactionId) {
        if (this.transactionIds == null) {
            this.transactionIds = new ArrayList<>(2);
        }
        this.transactionIds.add(transactionId);
    }

}
