package app.application.adapter.persistence.sqlserver;

import org.springframework.stereotype.Component;
import app.domain.models.AuditLog;
import app.domain.ports.AuditLogRepository;
import app.application.adapter.persistence.sqlserver.repositories.AuditLogJpaRepository;
import app.application.adapter.persistence.sqlserver.entities.AuditLogEntity;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlServerAuditLogRepository implements AuditLogRepository {
    private final AuditLogJpaRepository jpaRepository;

    public SqlServerAuditLogRepository(AuditLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(AuditLog auditLog) {
        jpaRepository.save(toJpaEntity(auditLog));
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream()
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByEntityId(String entityId) {
        return jpaRepository.findByEntityIdOrderByTimestampDesc(entityId).stream()
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }

    private AuditLog toDomainModel(AuditLogEntity entity) {
        return new AuditLog(
            entity.getAuditId(),
            entity.getUserId(),
            entity.getAction(),
            entity.getEntityType(),
            entity.getEntityId(),
            entity.getTimestamp(),
            entity.getDetails()
        );
    }

    private AuditLogEntity toJpaEntity(AuditLog auditLog) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.setAuditId(auditLog.getAuditId());
        entity.setUserId(auditLog.getUserId());
        entity.setAction(auditLog.getAction());
        entity.setEntityType(auditLog.getEntityType());
        entity.setEntityId(auditLog.getEntityId());
        entity.setTimestamp(auditLog.getTimestamp());
        entity.setDetails(auditLog.getDetails());
        return entity;
    }
}
