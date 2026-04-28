package app.domain.services;

import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import app.domain.services.commands.CreateTransferCommand;

public class CreateTransferService {
    private TransferRepository transferRepository;

    public CreateTransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Transfer execute(CreateTransferCommand command) {
        // Validate accounts are different
        if (command.getOriginAccount().equals(command.getDestinationAccount())) {
            throw new IllegalArgumentException("Origin and destination accounts must be different");
        }

        // Create transfer
        Transfer transfer = new Transfer(
            command.getTransferId(),
            command.getOriginAccount(),
            command.getDestinationAccount(),
            command.getAmount(),
            command.getCreatorUserId()
        );

        // Save transfer
        transferRepository.save(transfer);
        return transfer;
    }
}
