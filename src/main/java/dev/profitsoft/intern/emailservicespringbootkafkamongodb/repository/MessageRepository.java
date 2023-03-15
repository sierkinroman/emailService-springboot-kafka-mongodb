package dev.profitsoft.intern.emailservicespringbootkafkamongodb.repository;

import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.Message;
import dev.profitsoft.intern.emailservicespringbootkafkamongodb.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {

    Optional<Message> findByTransactionIdsIn(String transactionId);

    List<Message> findAllByStatus(MessageStatus status);

}
