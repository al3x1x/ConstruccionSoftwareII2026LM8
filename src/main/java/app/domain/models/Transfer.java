package app.domain.models;

import app.domain.enums.TransferStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Transfer {

    private String transferId;
    private String originAccount;
    private String destinationAccount;
    private double amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private TransferStatus status;
    private String creatorUserId;
    private String approverUserId;

    // Regla de negocio: transferencias de empresa por encima de este monto requieren aprobación
    public static final double APPROVAL_THRESHOLD = 10000.00;

    // Regla de negocio: si pasan más de 60 minutos sin aprobación, la transferencia vence
    public static final long EXPIRATION_MINUTES = 60;


    public Transfer(String transferId, String originAccount,
                    String destinationAccount, double amount, String creatorUserId) {
        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
        this.creationDate = LocalDateTime.now();

        // Si supera el umbral, requiere aprobación del supervisor
        if (amount > APPROVAL_THRESHOLD) {
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

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

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

    @Override
    public String toString() {
        return "Transfer{transferId='" + transferId + "', from='" + originAccount +
               "', to='" + destinationAccount + "', amount=" + amount + ", status=" + status + "}";
    }
}