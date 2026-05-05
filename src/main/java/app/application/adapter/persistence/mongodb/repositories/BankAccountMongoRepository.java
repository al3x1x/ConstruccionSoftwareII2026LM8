package app.application.adapter.persistence.mongodb.repositories;

import app.application.adapter.persistence.mongodb.documents.BankAccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BankAccountMongoRepository extends MongoRepository<BankAccountDocument, String> {
    List<BankAccountDocument> findByHolderId(String holderId);
}
