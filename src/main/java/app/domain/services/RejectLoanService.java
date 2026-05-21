package app.domain.services;

import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import app.domain.services.commands.RejectLoanCommand;
import app.domain.enums.LoanStatus;

public class RejectLoanService {
    private LoanRepository loanRepository;

    public RejectLoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void execute(RejectLoanCommand command) {
        Loan loan = loanRepository.findByLoanId(command.getLoanId())
            .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + command.getLoanId() + " not found"));

        // Validate loan is in UNDER_REVIEW status
        if (!LoanStatus.UNDER_REVIEW.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only loans in UNDER_REVIEW status can be rejected");
        }

        // Reject loan
        loan.reject();

        // Update persistence
        loanRepository.update(loan);
    }
}