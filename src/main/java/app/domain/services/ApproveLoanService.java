package app.domain.services;

import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import app.domain.services.commands.ApproveLoanCommand;
import app.domain.enums.LoanStatus;

public class ApproveLoanService {
    private LoanRepository loanRepository;

    public ApproveLoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void execute(ApproveLoanCommand command) {
        // Find loan
        Loan loan = loanRepository.findByLoanId(command.getLoanId());
        if (loan == null) {
            throw new IllegalArgumentException("Loan with ID " + command.getLoanId() + " not found");
        }

        // Validate loan is in UNDER_REVIEW status
        if (!LoanStatus.UNDER_REVIEW.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only loans in UNDER_REVIEW status can be approved");
        }

        // Approve loan
        loan.approve(command.getApprovedAmount(), command.getInterestRate());

        // Update persistence
        loanRepository.update(loan);
    }
}
