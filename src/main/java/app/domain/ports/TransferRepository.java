package app.domain.ports;

import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    Optional<Transfer> findByTransferId(String transferId);
    void save(Transfer transfer);
    List<Transfer> findByOriginAccount(String accountNumber);
    List<Transfer> findByDestinationAccount(String accountNumber);
    void update(Transfer transfer);
    boolean existsByTransferId(String transferId);
    
    List<Transfer> findByStatus(TransferStatus status);
}