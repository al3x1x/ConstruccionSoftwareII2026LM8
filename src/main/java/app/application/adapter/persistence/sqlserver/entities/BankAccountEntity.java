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

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "holder_id")
    private String holderId;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    private String currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    public BankAccountEntity() {}

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String v) { this.accountNumber = v; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType v) { this.accountType = v; }

    public String getHolderId() { return holderId; }
    public void setHolderId(String v) { this.holderId = v; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal v) { this.currentBalance = v; }

    public String getCurrency() { return currency; }
    public void setCurrency(String v) { this.currency = v; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus v) { this.status = v; }

    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate v) { this.openingDate = v; }
}