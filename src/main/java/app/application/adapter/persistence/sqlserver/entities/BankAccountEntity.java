package app.application.adapter.persistence.sqlserver.entities;

import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bank_accounts")
public class BankAccountEntity {

    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "holder_id", nullable = false)
    private String holderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "balance", nullable = false)
    private BigDecimal currentBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    // Constructors
    public BankAccountEntity() {}

    public BankAccountEntity(String accountNumber, String holderId, AccountType accountType,
                            BigDecimal currentBalance, AccountStatus status, String currency, LocalDate openingDate) {
        this.accountNumber = accountNumber;
        this.holderId = holderId;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.status = status;
        this.currency = currency;
        this.openingDate = openingDate;
        this.createdAt = LocalDate.now();
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getHolderId() { return holderId; }
    public void setHolderId(String holderId) { this.holderId = holderId; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
