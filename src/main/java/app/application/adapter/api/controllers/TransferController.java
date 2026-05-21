package app.application.adapter.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.application.usecases.CreateTransferUseCase;
import app.application.usecases.ApproveTransferUseCase;
import app.application.usecases.ExecuteTransferUseCase;
import app.domain.models.Transfer;
import app.domain.services.commands.CreateTransferCommand;
import app.domain.services.commands.ApproveTransferCommand;
import app.domain.services.commands.ExecuteTransferCommand;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final CreateTransferUseCase createTransferUseCase;
    private final ApproveTransferUseCase approveTransferUseCase;
    private final ExecuteTransferUseCase executeTransferUseCase;

    public TransferController(CreateTransferUseCase createTransferUseCase,
                            ApproveTransferUseCase approveTransferUseCase,
                            ExecuteTransferUseCase executeTransferUseCase) {
        this.createTransferUseCase = createTransferUseCase;
        this.approveTransferUseCase = approveTransferUseCase;
        this.executeTransferUseCase = executeTransferUseCase;
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(
            @RequestBody CreateTransferCommand command) {
        Transfer transfer = createTransferUseCase.execute(command);
        return ResponseEntity.ok(transfer);
    }

    @PostMapping("/{transferId}/approve")
    public ResponseEntity<Void> approveTransfer(
            @PathVariable String transferId,
            @RequestBody ApproveTransferCommand command) {
        command.setTransferId(transferId);
        approveTransferUseCase.execute(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{transferId}/execute")
    public ResponseEntity<Void> executeTransfer(
            @PathVariable String transferId,
            @RequestBody ExecuteTransferCommand command) {
        command.setTransferId(transferId);
        executeTransferUseCase.execute(command);
        return ResponseEntity.ok().build();
    }
}
