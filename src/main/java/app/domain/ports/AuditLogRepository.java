package app.domain.ports;

import app.domain.models.AuditLog;
import app.domain.enums.AuditOperationType;
import java.util.List;

public interface AuditLogRepository {
    void save(AuditLog log);
    List<AuditLog> findByUserId(String userId);
    List<AuditLog> findByOperationType(AuditOperationType operationType);
    List<AuditLog> findAll();
}
