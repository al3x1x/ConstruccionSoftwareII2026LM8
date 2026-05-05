package app.application.adapter.persistence.mongodb.repositories;

import app.application.adapter.persistence.mongodb.documents.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {
    List<LoanDocument> findByClientId(String clientId);
}
