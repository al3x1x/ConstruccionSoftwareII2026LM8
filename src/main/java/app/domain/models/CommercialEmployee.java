package app.domain.models;

import app.domain.enums.AccountType;
import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;
import app.domain.enums.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CommercialEmployee extends User {

    // ── Constructor ────────────────────────────────────────────────────

    public CommercialEmployee(String userId, String fullName, String identificationNumber,
                               String email, String phone, LocalDate birthDate,
                               String address, String username, String passwordHash) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.COMMERCIAL_EMPLOYEE, username, passwordHash);
    }


    public Loan createLoanRequest(String loanId, String clientId, LoanType loanType,
                                   BigDecimal requestedAmount, int termMonths) {
        return new Loan(loanId, loanType, clientId, requestedAmount, termMonths);
    }

    public BankAccount createAccountRequest(String accountNumber, AccountType accountType,
                                             String holderId, String currency) {
        return new BankAccount(accountNumber, accountType, holderId, currency);
    }

    public LoanStatus checkLoanStatus(Loan loan) {
        LoanStatus status = loan.getLoanStatus();
        if (!LoanStatus.UNDER_REVIEW.equals(status) && !LoanStatus.REJECTED.equals(status)) {
            throw new IllegalStateException("Commercial employees can only check loans UNDER_REVIEW or REJECTED.");
        }
        return status;
    }

    @Override
    public String toString() {
        return "CommercialEmployee{userId='" + getUserId() + "', name='" + getFullName() + "'}";
    }
}