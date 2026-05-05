package app.application.adapter.persistence.sqlserver.entities;

import app.domain.enums.TransferStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class TransferEntity {

    @Id
    @Column(name = "transfer_id")
    private String transferId;

    @Column(name = "origin_account", nullable = false)
    private String originAccount;

    @Column(name = "destination_account", nullable = false)
    private String destinationAccount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status;

    @Column(name = "creator_user_id", nullable = false)
    private String creatorUserId;

    @Column(name = "approver_user_id")
    private String approverUserId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public TransferEntity() {}

    public TransferEntity(String transferId, String originAccount, String destinationAccount,
                         BigDecimal amount, String creatorUserId, TransferStatus status) {
        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
        this.status = status;
        this.creationDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getTransferId() { return transferId; }
    public void setTransferId(String transferId) { this.transferId = transferId; }

    public String getOriginAccount() { return originAccount; }
    public void setOriginAccount(String originAccount) { this.originAccount = originAccount; }

    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }

    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }

    public String getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(String creatorUserId) { this.creatorUserId = creatorUserId; }

    public String getApproverUserId() { return approverUserId; }
    public void setApproverUserId(String approverUserId) { this.approverUserId = approverUserId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
