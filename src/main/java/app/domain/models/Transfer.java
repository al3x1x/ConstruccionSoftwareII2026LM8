package app.domain.models;

import app.domain.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Transfer {

    private String transferId;
    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private TransferStatus status;
    private String creatorUserId;
    private String approverUserId;
    private String assignedCommercialEmployeeId;

    // Business rule: enterprise transfers above this amount require approval
    public static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("10000.00");

    // Business rule: if more than 60 minutes pass without approval, transfer expires
    public static final long EXPIRATION_MINUTES = 60;


    public Transfer(String transferId, String originAccount,
                    String destinationAccount, BigDecimal amount, String creatorUserId) {
        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
        this.creationDate = LocalDateTime.now();

        // If exceeds threshold, approval from supervisor is required
        if (amount.compareTo(APPROVAL_THRESHOLD) > 0) {
            this.status = TransferStatus.AWAITING_APPROVAL;
        } else {
            this.status = TransferStatus.PENDING;
        }
    }


    public void execute(BankAccount origin, BankAccount destination) {
        origin.debit(this.amount);
        destination.credit(this.amount);
        this.status = TransferStatus.EXECUTED;
    }

    public void approve(String approverUserId) {
        this.approverUserId = approverUserId;
        this.approvalDate = LocalDateTime.now();
        this.status = TransferStatus.APPROVED;
    }

    public void reject(String approverUserId) {
        this.approverUserId = approverUserId;
        this.status = TransferStatus.REJECTED;
    }

    public boolean checkExpiration() {
        if (!TransferStatus.AWAITING_APPROVAL.equals(this.status)) return false;

        long minutesElapsed = ChronoUnit.MINUTES.between(this.creationDate, LocalDateTime.now());

        if (minutesElapsed > EXPIRATION_MINUTES) {
            this.status = TransferStatus.EXPIRED;
            return true;
        }
        return false;
    }


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

    public String getAssignedCommercialEmployeeId() { return assignedCommercialEmployeeId; }
    public void setAssignedCommercialEmployeeId(String assignedCommercialEmployeeId) {
        this.assignedCommercialEmployeeId = assignedCommercialEmployeeId;
    }

    @Override
    public String toString() {
        return "Transfer{transferId='" + transferId + "', from='" + originAccount +
               "', to='" + destinationAccount + "', amount=" + amount + ", status=" + status + "}";
    }
}