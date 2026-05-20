package app.application.adapter.persistence.sqlserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.application.adapter.persistence.sqlserver.entities.AuditLogEntity;
import java.util.List;

@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, String> {
    List<AuditLogEntity> findByUserId(String userId);
    List<AuditLogEntity> findByEntityIdOrderByTimestampDesc(String entityId);
}
