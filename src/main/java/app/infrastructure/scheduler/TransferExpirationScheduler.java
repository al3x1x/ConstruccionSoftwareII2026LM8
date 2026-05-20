package app.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import app.domain.models.Transfer;
import app.domain.models.AuditLog;
import app.domain.ports.TransferRepository;
import app.domain.ports.AuditLogRepository;
import app.domain.enums.TransferStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TransferExpirationScheduler {
    private final TransferRepository transferRepository;
    private final AuditLogRepository auditLogRepository;

    public TransferExpirationScheduler(TransferRepository transferRepository,
                                      AuditLogRepository auditLogRepository) {
        this.transferRepository = transferRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Scheduled(fixedRate = 180000)
    public void checkAndMarkExpiredTransfers() {
        List<Transfer> awaitingApprovalTransfers = transferRepository.findByStatus(
            TransferStatus.AWAITING_APPROVAL);

        for (Transfer transfer : awaitingApprovalTransfers) {
            if (transfer.checkExpiration()) {
                transferRepository.update(transfer);

                AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    "SYSTEM",
                    "TRANSFER_EXPIRED",
                    "Transfer",
                    transfer.getTransferId(),
                    LocalDateTime.now(),
                    "Transfer expirado después de 60 minutos sin aprobación"
                );
                auditLogRepository.save(log);
            }
        }
    }
}
