package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.AuditLogEntity;
import app.domain.enums.AuditOperationType;
import app.domain.enums.UserRole;
import app.domain.models.AuditLog;
import app.domain.ports.AuditLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

interface AuditLogRepositorySpringData extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByExecutorUserId(String executorUserId);
}

@Repository
class AuditLogRepositoryImpl implements AuditLogRepository {
    private final AuditLogRepositorySpringData springDataRepo;

    public AuditLogRepositoryImpl(AuditLogRepositorySpringData springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    private AuditLogEntity toEntity(AuditLog d) {
        AuditLogEntity e = new AuditLogEntity();
        e.setAuditLogId(d.getAuditLogId());
        e.setOperationType(d.getOperationType().toString());
        e.setOperationDateTime(d.getOperationDateTime());
        e.setExecutorUserId(d.getExecutorUserId());
        e.setExecutorUserRole(d.getExecutorUserRole().toString());
        e.setAffectedProductId(d.getAffectedProductId());
        return e;
    }

    private AuditLog toDomain(AuditLogEntity e) {
        AuditLog log = new AuditLog(e.getAuditLogId(), AuditOperationType.valueOf(e.getOperationType()),
                           e.getExecutorUserId(), UserRole.valueOf(e.getExecutorUserRole()), e.getAffectedProductId());
        return log;
    }

    @Override
    public void save(AuditLog log) {
        springDataRepo.save(toEntity(log));
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return springDataRepo.findByExecutorUserId(userId)
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByOperationType(AuditOperationType operationType) {
        return springDataRepo.findAll().stream()
            .filter(e -> e.getOperationType().equals(operationType.toString()))
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findAll() {
        return springDataRepo.findAll().stream()
            .map(this::toDomain).collect(Collectors.toList());
    }
}
