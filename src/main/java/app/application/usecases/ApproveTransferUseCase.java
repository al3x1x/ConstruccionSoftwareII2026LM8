package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import app.domain.services.commands.ApproveTransferCommand;
import app.domain.enums.TransferStatus;

@Component
public class ApproveTransferUseCase {
    private TransferRepository transferRepository;

    public ApproveTransferUseCase(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public void execute(ApproveTransferCommand command) {
        Transfer transfer = transferRepository.findByTransferId(command.getTransferId())
            .orElseThrow(() -> new IllegalArgumentException("Transfer with ID " + command.getTransferId() + " not found"));

        if (!TransferStatus.AWAITING_APPROVAL.equals(transfer.getStatus())) {
            throw new IllegalStateException("Only transfers AWAITING_APPROVAL can be approved");
        }

        transfer.approve(command.getApproverUserId());
        transferRepository.update(transfer);
    }
}
