package app.domain.services.commands;

import app.domain.enums.AccountType;

public class CreateBankAccountCommand {
    private String accountNumber;
    private AccountType accountType;
    private String holderId;
    private String currency;

    public CreateBankAccountCommand(String accountNumber, AccountType accountType,
                                   String holderId, String currency) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.holderId = holderId;
        this.currency = currency;
    }

    public String getAccountNumber() { return accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public String getHolderId() { return holderId; }
    public String getCurrency() { return currency; }
}
