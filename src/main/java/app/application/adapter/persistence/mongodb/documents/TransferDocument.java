package app.application.adapter.persistence.mongodb.documents;

import app.domain.enums.TransferStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transfers")
public class TransferDocument {

    @Id
    private String transferId;

    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private TransferStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private String creatorUserId;
    private String approverUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TransferDocument() {}

    public TransferDocument(String transferId, String originAccount, String destinationAccount,
                           BigDecimal amount, String creatorUserId) {
        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
        this.status = TransferStatus.PENDING;
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

    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }

    public String getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(String creatorUserId) { this.creatorUserId = creatorUserId; }

    public String getApproverUserId() { return approverUserId; }
    public void setApproverUserId(String approverUserId) { this.approverUserId = approverUserId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
