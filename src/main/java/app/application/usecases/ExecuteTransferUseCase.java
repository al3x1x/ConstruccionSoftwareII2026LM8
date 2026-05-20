package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.Transfer;
import app.domain.models.BankAccount;
import app.domain.ports.TransferRepository;
import app.domain.ports.BankAccountRepository;
import app.domain.services.commands.ExecuteTransferCommand;
import app.domain.enums.TransferStatus;
import app.domain.exceptions.InvalidTransferException;
import app.domain.exceptions.InsufficientFundsException;

@Component
public class ExecuteTransferUseCase {
    private TransferRepository transferRepository;
    private BankAccountRepository bankAccountRepository;

    public ExecuteTransferUseCase(TransferRepository transferRepository,
                                  BankAccountRepository bankAccountRepository) {
        this.transferRepository = transferRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void execute(ExecuteTransferCommand command) {
        Transfer transfer = transferRepository.findByTransferId(command.getTransferId())
            .orElseThrow(() -> new IllegalArgumentException("Transfer with ID " + command.getTransferId() + " not found"));

        if (!TransferStatus.APPROVED.equals(transfer.getStatus()) &&
            !TransferStatus.PENDING.equals(transfer.getStatus())) {
            throw new InvalidTransferException("Transfer must be APPROVED or PENDING to be executed. Current status: " + transfer.getStatus());
        }

        BankAccount originAccount = bankAccountRepository.findByAccountNumber(transfer.getOriginAccount())
            .orElseThrow(() -> new IllegalArgumentException("Origin account not found"));

        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(transfer.getDestinationAccount())
            .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (originAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in origin account");
        }

        transfer.execute(originAccount, destinationAccount);

        transferRepository.update(transfer);
        bankAccountRepository.update(originAccount);
        bankAccountRepository.update(destinationAccount);
    }
}
