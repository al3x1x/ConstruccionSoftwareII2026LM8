package app.domain.services;

import app.domain.models.Loan;
import app.domain.models.BankAccount;
import app.domain.ports.LoanRepository;
import app.domain.ports.BankAccountRepository;
import app.domain.services.commands.DisburseLoanCommand;
import app.domain.enums.LoanStatus;
import app.domain.exceptions.LoanAlreadyDisbursedException;
import app.domain.exceptions.InsufficientFundsException;

public class DisburseLoanService {
    private LoanRepository loanRepository;
    private BankAccountRepository bankAccountRepository;

    public DisburseLoanService(LoanRepository loanRepository,
                               BankAccountRepository bankAccountRepository) {
        this.loanRepository = loanRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void execute(DisburseLoanCommand command) {
        // 1. CORREGIDO: Buscamos el préstamo usando orElseThrow() ya que ahora retorna Optional
        Loan loan = loanRepository.findByLoanId(command.getLoanId())
            .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + command.getLoanId() + " not found"));

        // Validate loan is APPROVED
        if (!LoanStatus.APPROVED.equals(loan.getLoanStatus())) {
            throw new IllegalStateException("Only APPROVED loans can be disbursed");
        }

        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(command.getDestinationAccountNumber())
            .orElseThrow(() -> new IllegalArgumentException("Account with number " + command.getDestinationAccountNumber() + " not found"));

        // Disburse the loan
        loan.disburse(destinationAccount);

        // Update persistence
        loanRepository.update(loan);
        bankAccountRepository.update(destinationAccount);
    }
}