package app.application.adapter.persistence.sqlserver.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class TransferEntity {
    @Id
    private String transferId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private LocalDate transferDate;
    private LocalDateTime creationDate;
    private String status;
    private String creatorUserId;
    private String approverUserId;
    private LocalDateTime approvalDate;

    public TransferEntity() {}

    public String getTransferId() { return transferId; }
    public void setTransferId(String v) { this.transferId = v; }

    public String getFromAccountNumber() { return fromAccountNumber; }
    public void setFromAccountNumber(String v) { this.fromAccountNumber = v; }

    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String v) { this.toAccountNumber = v; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal v) { this.amount = v; }

    public LocalDate getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDate v) { this.transferDate = v; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime v) { this.creationDate = v; }

    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }

    public String getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(String v) { this.creatorUserId = v; }

    public String getApproverUserId() { return approverUserId; }
    public void setApproverUserId(String v) { this.approverUserId = v; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime v) { this.approvalDate = v; }
}
