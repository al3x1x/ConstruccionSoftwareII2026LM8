package app.domain.services;

import app.domain.models.Transfer;
import app.domain.models.BankAccount;
import app.domain.ports.TransferRepository;
import app.domain.ports.BankAccountRepository;
import app.domain.services.commands.ExecuteTransferCommand;
import app.domain.enums.TransferStatus;
import app.domain.exceptions.InvalidTransferException;
import app.domain.exceptions.InsufficientFundsException;

public class ExecuteTransferService {
    private TransferRepository transferRepository;
    private BankAccountRepository bankAccountRepository;

    public ExecuteTransferService(TransferRepository transferRepository,
                                  BankAccountRepository bankAccountRepository) {
        this.transferRepository = transferRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void execute(ExecuteTransferCommand command) {
        // Find transfer
        Transfer transfer = transferRepository.findByTransferId(command.getTransferId());
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer with ID " + command.getTransferId() + " not found");
        }

        // Validate transfer can be executed (APPROVED or PENDING)
        if (!TransferStatus.APPROVED.equals(transfer.getStatus()) && !TransferStatus.PENDING.equals(transfer.getStatus())) {
            throw new InvalidTransferException("Transfer must be APPROVED or PENDING to be executed. Current status: " + transfer.getStatus());
        }

        // Find accounts
        BankAccount originAccount = bankAccountRepository.findByAccountNumber(transfer.getOriginAccount());
        if (originAccount == null) {
            throw new IllegalArgumentException("Origin account not found");
        }

        BankAccount destinationAccount = bankAccountRepository.findByAccountNumber(transfer.getDestinationAccount());
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account not found");
        }

        // Validate origin account has sufficient funds
        if (originAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in origin account");
        }

        // Execute transfer
        transfer.execute(originAccount, destinationAccount);

        // Update persistence
        transferRepository.update(transfer);
        bankAccountRepository.update(originAccount);
        bankAccountRepository.update(destinationAccount);
    }
}
