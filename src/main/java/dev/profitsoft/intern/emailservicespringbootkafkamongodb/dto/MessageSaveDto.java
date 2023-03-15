package dev.profitsoft.intern.emailservicespringbootkafkamongodb.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MessageSaveDto {

    private String subject;

    private String content;

    private Set<String> emailsTo;

}
