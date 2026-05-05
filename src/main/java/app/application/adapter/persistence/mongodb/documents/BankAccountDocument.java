package app.application.adapter.persistence.mongodb.documents;

import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "bank_accounts")
public class BankAccountDocument {

    @Id
    private String accountNumber;

    private String holderId;
    private AccountType accountType;
    private BigDecimal currentBalance;
    private AccountStatus status;
    private String currency;
    private LocalDate openingDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Constructors
    public BankAccountDocument() {}

    public BankAccountDocument(String accountNumber, String holderId, AccountType accountType,
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
