package app.application.adapter.persistence.mongodb;

import app.application.adapter.persistence.mongodb.documents.AuditLogDocument;
import app.application.adapter.persistence.mongodb.repositories.AuditLogMongoRepository;
import app.domain.models.AuditLog;
import app.domain.enums.AuditOperationType;
import app.domain.ports.AuditLogRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoDbAuditLogRepository implements AuditLogRepository {
    private final AuditLogMongoRepository mongoRepository;

    public MongoDbAuditLogRepository(AuditLogMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void save(AuditLog log) {
        AuditLogDocument document = toDocument(log);
        mongoRepository.save(document);
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return mongoRepository.findByUserId(userId)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByOperationType(AuditOperationType operationType) {
        return mongoRepository.findByOperationType(operationType)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    private AuditLog toDomainModel(AuditLogDocument document) {
        AuditLog log = new AuditLog(
            document.getId(),
            document.getOperationType(),
            document.getUserId(),
            document.getUserRole(),
            document.getEntityId()
        );
        log.setOperationDateTime(document.getTimestamp());
        return log;
    }

    private AuditLogDocument toDocument(AuditLog log) {
        AuditLogDocument document = new AuditLogDocument();
        document.setId(log.getAuditLogId());
        document.setOperationType(log.getOperationType());
        document.setUserId(log.getExecutorUserId());
        document.setUserRole(log.getExecutorUserRole());
        document.setEntityId(log.getAffectedProductId());
        document.setTimestamp(log.getOperationDateTime());
        return document;
    }
}
