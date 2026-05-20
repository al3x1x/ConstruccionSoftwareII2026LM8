package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import app.domain.services.commands.RejectLoanCommand;
import app.domain.enums.LoanStatus;

@Component
public class RejectLoanUseCase {
    private LoanRepository loanRepository;

    public RejectLoanUseCase(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void execute(RejectLoanCommand command) {
        Loan loan = loanRepository.findByLoanId(command.getLoanId())
            .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + command.getLoanId() + " not found"));

        if (!LoanStatus.UNDER_REVIEW.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only loans in UNDER_REVIEW status can be rejected");
        }

        loan.reject();
        loanRepository.update(loan);
    }
}
