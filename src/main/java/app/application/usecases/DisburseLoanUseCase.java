package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.Loan;
import app.domain.models.BankAccount;
import app.domain.ports.LoanRepository;
import app.domain.ports.BankAccountRepository;
import app.domain.services.commands.DisburseLoanCommand;
import app.domain.enums.LoanStatus;

@Component
public class DisburseLoanUseCase {
    private LoanRepository loanRepository;
    private BankAccountRepository bankAccountRepository;

    public DisburseLoanUseCase(LoanRepository loanRepository,
                               BankAccountRepository bankAccountRepository) {
        this.loanRepository = loanRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void execute(DisburseLoanCommand command) {
        Loan loan = loanRepository.findByLoanId(command.getLoanId())
            .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + command.getLoanId() + " not found"));

        if (!LoanStatus.APPROVED.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only APPROVED loans can be disbursed");
        }

        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(command.getDestinationAccountNumber())
            .orElseThrow(() -> new IllegalArgumentException("Account with number " + command.getDestinationAccountNumber() + " not found"));

        loan.disburse(destinationAccount);

        loanRepository.update(loan);
        bankAccountRepository.update(destinationAccount);
    }
}
