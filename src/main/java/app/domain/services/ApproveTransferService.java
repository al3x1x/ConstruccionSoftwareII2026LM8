package app.domain.services;

import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import app.domain.services.commands.ApproveTransferCommand;
import app.domain.enums.TransferStatus;

public class ApproveTransferService {
    private TransferRepository transferRepository;

    public ApproveTransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public void execute(ApproveTransferCommand command) {
        // Find transfer
        Transfer transfer = transferRepository.findByTransferId(command.getTransferId());
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer with ID " + command.getTransferId() + " not found");
        }

        // Validate transfer is awaiting approval
        if (!TransferStatus.AWAITING_APPROVAL.equals(transfer.getStatus())) {
            throw new IllegalStateException("Only transfers AWAITING_APPROVAL can be approved");
        }

        // Approve transfer
        transfer.approve(command.getApproverUserId());

        // Update persistence
        transferRepository.update(transfer);
    }
}
