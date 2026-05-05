package app.application.adapter.persistence.mongodb.repositories;

import app.application.adapter.persistence.mongodb.documents.TransferDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransferMongoRepository extends MongoRepository<TransferDocument, String> {
    List<TransferDocument> findByOriginAccount(String accountNumber);
    List<TransferDocument> findByDestinationAccount(String accountNumber);
}
