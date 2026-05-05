package app.application.adapter.persistence.mongodb.repositories;

import app.application.adapter.persistence.mongodb.documents.AuditLogDocument;
import app.domain.enums.AuditOperationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
    List<AuditLogDocument> findByUserId(String userId);
    List<AuditLogDocument> findByOperationType(AuditOperationType operationType);
}
