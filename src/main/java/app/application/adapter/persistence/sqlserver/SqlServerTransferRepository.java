package app.application.adapter.persistence.sqlserver;

import app.application.adapter.persistence.sqlserver.entities.TransferEntity;
import app.application.adapter.persistence.sqlserver.repositories.TransferJpaRepository;
import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.ports.TransferRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlServerTransferRepository implements TransferRepository {
    private final TransferJpaRepository jpaRepository;

    public SqlServerTransferRepository(TransferJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transfer findByTransferId(String transferId) {
        return jpaRepository.findById(transferId)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(Transfer transfer) {
        TransferEntity entity = toJpaEntity(transfer);
        jpaRepository.save(entity);
    }

    @Override
    public List<Transfer> findByOriginAccount(String originAccount) {
        return jpaRepository.findByOriginAccount(originAccount)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByDestinationAccount(String destinationAccount) {
        return jpaRepository.findByDestinationAccount(destinationAccount)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Transfer transfer) {
        save(transfer);
    }

    @Override
    public boolean existsByTransferId(String transferId) {
        return jpaRepository.existsById(transferId);
    }

    private Transfer toDomainModel(TransferEntity entity) {
        Transfer transfer = new Transfer(
            entity.getTransferId(),
            entity.getOriginAccount(),
            entity.getDestinationAccount(),
            entity.getAmount(),
            entity.getCreatorUserId()
        );
        transfer.setStatus(entity.getStatus());
        transfer.setCreationDate(entity.getCreationDate());
        transfer.setApprovalDate(entity.getApprovalDate());
        transfer.setApproverUserId(entity.getApproverUserId());
        return transfer;
    }

    private TransferEntity toJpaEntity(Transfer transfer) {
        TransferEntity entity = new TransferEntity();
        entity.setTransferId(transfer.getTransferId());
        entity.setOriginAccount(transfer.getOriginAccount());
        entity.setDestinationAccount(transfer.getDestinationAccount());
        entity.setAmount(transfer.getAmount());
        entity.setCreatorUserId(transfer.getCreatorUserId());
        entity.setStatus(transfer.getStatus());
        entity.setCreationDate(transfer.getCreationDate());
        entity.setApprovalDate(transfer.getApprovalDate());
        entity.setApproverUserId(transfer.getApproverUserId());
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        return entity;
    }
}
