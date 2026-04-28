package app.domain.models;

import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BankAccount {

    private String accountNumber;
    private AccountType accountType;
    private String holderId;
    private BigDecimal currentBalance;
    private String currency;
    private AccountStatus status;
    private LocalDate openingDate;

    // ── Constructor ────────────────────────────────────────────────────

    public BankAccount(String accountNumber, AccountType accountType,
                       String holderId, String currency) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.holderId = holderId;
        this.currency = currency;
        this.currentBalance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
        this.openingDate = LocalDate.now();
    }

    // ── Business Methods ────────────────────────────────────────────────

    public void credit(BigDecimal amount) {
        this.currentBalance = this.currentBalance.add(amount);
    }

    public void debit(BigDecimal amount) {
        this.currentBalance = this.currentBalance.subtract(amount);
    }

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }

    // ── Getters & Setters ──────────────────────────────────────────────

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public String getHolderId() { return holderId; }
    public void setHolderId(String holderId) { this.holderId = holderId; }

    public BigDecimal getCurrentBalance() { return currentBalance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }

    @Override
    public String toString() {
        return "BankAccount{accountNumber='" + accountNumber + "', holder='" + holderId +
               "', balance=" + currentBalance + ", status=" + status + "}";
    }
}