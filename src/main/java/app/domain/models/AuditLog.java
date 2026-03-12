package app.domain.models;

import app.domain.enums.AuditOperationType;

import java.time.LocalDateTime;

public class AuditLog {

    private String auditLogId;
    private AuditOperationType operationType;
    private LocalDateTime operationDateTime;
    private String executorUserId;
    private String executorUserRole;
    private String affectedProductId;

    // Solo uno de estos tres tendrá valor según el tipo de operación
    private TransferDetail transferDetail;
    private LoanDetail loanDetail;
    private ExpirationDetail expirationDetail;

    // ── Constructor ───────────────────────────────────────────────────

    public AuditLog(String auditLogId, AuditOperationType operationType,
                     String executorUserId, String executorUserRole,
                     String affectedProductId) {
        this.auditLogId = auditLogId;
        this.operationType = operationType;
        this.executorUserId = executorUserId;
        this.executorUserRole = executorUserRole;
        this.affectedProductId = affectedProductId;
        this.operationDateTime = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getAuditLogId() { return auditLogId; }
    public void setAuditLogId(String auditLogId) { this.auditLogId = auditLogId; }

    public AuditOperationType getOperationType() { return operationType; }
    public void setOperationType(AuditOperationType operationType) { this.operationType = operationType; }

    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public void setOperationDateTime(LocalDateTime operationDateTime) { this.operationDateTime = operationDateTime; }

    public String getExecutorUserId() { return executorUserId; }
    public void setExecutorUserId(String executorUserId) { this.executorUserId = executorUserId; }

    public String getExecutorUserRole() { return executorUserRole; }
    public void setExecutorUserRole(String executorUserRole) { this.executorUserRole = executorUserRole; }

    public String getAffectedProductId() { return affectedProductId; }
    public void setAffectedProductId(String affectedProductId) { this.affectedProductId = affectedProductId; }

    public TransferDetail getTransferDetail() { return transferDetail; }
    public void setTransferDetail(TransferDetail transferDetail) { this.transferDetail = transferDetail; }

    public LoanDetail getLoanDetail() { return loanDetail; }
    public void setLoanDetail(LoanDetail loanDetail) { this.loanDetail = loanDetail; }

    public ExpirationDetail getExpirationDetail() { return expirationDetail; }
    public void setExpirationDetail(ExpirationDetail expirationDetail) { this.expirationDetail = expirationDetail; }

    @Override
    public String toString() {
        return "AuditLog{auditLogId='" + auditLogId + "', operation=" + operationType +
               ", dateTime=" + operationDateTime + ", userId='" + executorUserId + "'}";
    }
}