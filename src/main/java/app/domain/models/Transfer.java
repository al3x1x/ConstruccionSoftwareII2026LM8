package app.domain.models;

import app.domain.enums.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Transfer {

    public static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("10000.00");
    public static final long EXPIRATION_MINUTES = 60;

    private String transferId;
    private String originAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private LocalDate transferDate;
    private LocalDateTime creationDate;
    private TransferStatus status;
    private String creatorUserId;
    private String approverUserId;
    private LocalDateTime approvalDate;

    public Transfer(String transferId, String originAccountNumber,
                    String destinationAccountNumber, BigDecimal amount,
                    String creatorUserId) {
        this.transferId = transferId;
        this.originAccountNumber = originAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
        this.transferDate = LocalDate.now();
        this.creationDate = LocalDateTime.now();
        if (amount.compareTo(APPROVAL_THRESHOLD) > 0) {
            this.status = TransferStatus.AWAITING_APPROVAL;
        } else {
            this.status = TransferStatus.PENDING;
        }
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

    public void execute(BankAccount origin, BankAccount destination) {
        origin.debit(this.amount);
        destination.credit(this.amount);
        this.status = TransferStatus.EXECUTED;
    }

    public boolean checkExpiration() {
        if (!TransferStatus.AWAITING_APPROVAL.equals(this.status)) return false;
        long minutes = ChronoUnit.MINUTES.between(this.creationDate, LocalDateTime.now());
        if (minutes > EXPIRATION_MINUTES) {
            this.status = TransferStatus.EXPIRED;
            return true;
        }
        return false;
    }

    public String getOriginAccount() { return originAccountNumber; }
    public String getDestinationAccount() { return destinationAccountNumber; }
    public String getTransferId() { return transferId; }
    public String getOriginAccountNumber() { return originAccountNumber; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getTransferDate() { return transferDate; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public String getCreatorUserId() { return creatorUserId; }
    public String getApproverUserId() { return approverUserId; }
    public LocalDateTime getApprovalDate() { return approvalDate; }
}