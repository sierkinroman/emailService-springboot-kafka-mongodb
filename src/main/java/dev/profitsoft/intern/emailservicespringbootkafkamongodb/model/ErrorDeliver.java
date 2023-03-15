package dev.profitsoft.intern.emailservicespringbootkafkamongodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ErrorDeliver {

    private String exceptionClass;

    private String exceptionMessage;

}
