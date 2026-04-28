package app.application.adapter.persistence;

import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTransferRepository implements TransferRepository {
    private Map<String, Transfer> transfers = new HashMap<>();

    @Override
    public Transfer findByTransferId(String transferId) {
        return transfers.get(transferId);
    }

    @Override
    public void save(Transfer transfer) {
        transfers.put(transfer.getTransferId(), transfer);
    }

    @Override
    public List<Transfer> findByOriginAccount(String accountNumber) {
        return transfers.values().stream()
            .filter(t -> accountNumber.equals(t.getOriginAccount()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByDestinationAccount(String accountNumber) {
        return transfers.values().stream()
            .filter(t -> accountNumber.equals(t.getDestinationAccount()))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Transfer transfer) {
        transfers.put(transfer.getTransferId(), transfer);
    }

    @Override
    public boolean existsByTransferId(String transferId) {
        return transfers.containsKey(transferId);
    }
}
