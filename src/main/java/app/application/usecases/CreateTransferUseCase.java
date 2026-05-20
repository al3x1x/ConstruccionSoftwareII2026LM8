package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import app.domain.services.commands.CreateTransferCommand;

@Component
public class CreateTransferUseCase {
    private TransferRepository transferRepository;

    public CreateTransferUseCase(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Transfer execute(CreateTransferCommand command) {
        if (command.getOriginAccount().equals(command.getDestinationAccount())) {
            throw new IllegalArgumentException("Origin and destination accounts must be different");
        }

        Transfer transfer = new Transfer(
            command.getTransferId(),
            command.getOriginAccount(),
            command.getDestinationAccount(),
            command.getAmount(),
            command.getCreatorUserId()
        );

        transferRepository.save(transfer);
        return transfer;
    }
}
