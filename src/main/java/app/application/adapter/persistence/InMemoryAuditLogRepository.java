package app.application.adapter.persistence;

import app.domain.models.AuditLog;
import app.domain.enums.AuditOperationType;
import app.domain.ports.AuditLogRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryAuditLogRepository implements AuditLogRepository {
    private List<AuditLog> auditLogs = new ArrayList<>();

    @Override
    public void save(AuditLog log) {
        auditLogs.add(log);
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return auditLogs.stream()
            .filter(l -> userId.equals(l.getExecutorUserId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByOperationType(AuditOperationType operationType) {
        return auditLogs.stream()
            .filter(l -> operationType.equals(l.getOperationType()))
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findAll() {
        return List.copyOf(auditLogs);
    }
}
