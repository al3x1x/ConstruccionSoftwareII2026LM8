package app.domain.ports;

import app.domain.models.Transfer;
import java.util.List;

public interface TransferRepository {
    Transfer findByTransferId(String transferId);
    void save(Transfer transfer);
    List<Transfer> findByOriginAccount(String accountNumber);
    List<Transfer> findByDestinationAccount(String accountNumber);
    void update(Transfer transfer);
    boolean existsByTransferId(String transferId);
}
