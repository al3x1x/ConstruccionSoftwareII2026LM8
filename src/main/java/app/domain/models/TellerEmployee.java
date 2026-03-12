package app.domain.models;

import app.domain.enums.AccountType;
import app.domain.enums.UserRole;

import java.time.LocalDate;

public class TellerEmployee extends User {

    // ── Constructor ───────────────────────────────────────────────────

    public TellerEmployee(String userId, String fullName, String identificationNumber,
                           String email, String phone, LocalDate birthDate,
                           String address, String username, String passwordHash) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.TELLER_EMPLOYEE, username, passwordHash);
    }

    // ── Métodos de negocio ────────────────────────────────────────────

    public void deposit(BankAccount account, double amount) {
        if (!account.isActive()) {
            throw new IllegalStateException("Cannot deposit into a non-active account.");
        }
        account.credit(amount);
    }

    public void withdrawal(BankAccount account, double amount) {
        if (!account.isActive()) {
            throw new IllegalStateException("Cannot withdraw from a non-active account.");
        }
        account.debit(amount);
    }

    public BankAccount openAccount(String accountNumber, AccountType accountType,
                                    String holderId, String currency) {
        return new BankAccount(accountNumber, accountType, holderId, currency);
    }

    @Override
    public String toString() {
        return "TellerEmployee{userId='" + getUserId() + "', name='" + getFullName() + "'}";
    }
}