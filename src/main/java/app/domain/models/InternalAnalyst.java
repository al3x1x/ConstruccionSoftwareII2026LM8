package app.domain.models;

import app.domain.enums.LoanStatus;
import app.domain.enums.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InternalAnalyst extends User {

    // ── Constructor ───────────────────────────────────────────────────

    public InternalAnalyst(String userId, String fullName, String identificationNumber,
                            String email, String phone, LocalDate birthDate,
                            String address, String username, String passwordHash) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.INTERNAL_ANALYST, username, passwordHash);
    }

    // ── Métodos de negocio ────────────────────────────────────────────

    public void approveLoan(Loan loan, BigDecimal approvedAmount, BigDecimal interestRate) {
        if (!LoanStatus.UNDER_REVIEW.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only loans UNDER_REVIEW can be approved.");
        }
        loan.approve(approvedAmount, interestRate);
    }

    public void rejectLoan(Loan loan) {
        if (!LoanStatus.UNDER_REVIEW.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only loans UNDER_REVIEW can be rejected.");
        }
        loan.reject();
    }

    public void disburseLoan(Loan loan, BankAccount destinationAccount) {
        if (!LoanStatus.APPROVED.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only APPROVED loans can be disbursed.");
        }
        if (!destinationAccount.isActive()) {
            throw new IllegalStateException("Destination account must be ACTIVE.");
        }
        if (!destinationAccount.getHolderId().equals(loan.getClientId())) {
            throw new IllegalStateException("Account does not belong to the loan client.");
        }
        loan.disburse(destinationAccount);
    }

    @Override
    public String toString() {
        return "InternalAnalyst{userId='" + getUserId() + "', name='" + getFullName() + "'}";
    }
}