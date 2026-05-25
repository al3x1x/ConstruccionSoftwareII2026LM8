package app.application.adapter.persistence.sqlserver.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String auditLogId;
    private String operationType;
    private LocalDateTime operationDateTime;
    private String executorUserId;
    private String executorUserRole;
    private String affectedProductId;

    public AuditLogEntity() {}

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }

    public String getAuditLogId() { return auditLogId; }
    public void setAuditLogId(String v) { this.auditLogId = v; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String v) { this.operationType = v; }

    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public void setOperationDateTime(LocalDateTime v) { this.operationDateTime = v; }

    public String getExecutorUserId() { return executorUserId; }
    public void setExecutorUserId(String v) { this.executorUserId = v; }

    public String getExecutorUserRole() { return executorUserRole; }
    public void setExecutorUserRole(String v) { this.executorUserRole = v; }

    public String getAffectedProductId() { return affectedProductId; }
    public void setAffectedProductId(String v) { this.affectedProductId = v; }
}
