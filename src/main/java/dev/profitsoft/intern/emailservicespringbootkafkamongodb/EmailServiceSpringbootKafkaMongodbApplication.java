package dev.profitsoft.intern.emailservicespringbootkafkamongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailServiceSpringbootKafkaMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailServiceSpringbootKafkaMongodbApplication.class, args);
    }

}
