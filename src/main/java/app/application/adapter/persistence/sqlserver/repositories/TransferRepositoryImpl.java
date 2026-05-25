package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.TransferEntity;
import app.domain.enums.TransferStatus;
import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface TransferRepositorySpringData extends JpaRepository<TransferEntity, String> {
    List<TransferEntity> findByFromAccountNumber(String accountNumber);
    List<TransferEntity> findByToAccountNumber(String accountNumber);
}

@Repository
class TransferRepositoryImpl implements TransferRepository {
    private final TransferRepositorySpringData springDataRepo;

    public TransferRepositoryImpl(TransferRepositorySpringData springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    private TransferEntity toEntity(Transfer d) {
        TransferEntity e = new TransferEntity();
        e.setTransferId(d.getTransferId());
        e.setFromAccountNumber(d.getOriginAccountNumber());
        e.setToAccountNumber(d.getDestinationAccountNumber());
        e.setAmount(d.getAmount());
        e.setTransferDate(d.getTransferDate());
        e.setCreationDate(d.getCreationDate());
        e.setStatus(d.getStatus().toString());
        e.setCreatorUserId(d.getCreatorUserId());
        e.setApproverUserId(d.getApproverUserId());
        e.setApprovalDate(d.getApprovalDate());
        return e;
    }

    private Transfer toDomain(TransferEntity e) {
        return new Transfer(e.getTransferId(), e.getFromAccountNumber(),
                           e.getToAccountNumber(), e.getAmount(), e.getCreatorUserId());
    }

    @Override
    public Optional<Transfer> findByTransferId(String transferId) {
        return springDataRepo.findById(transferId).map(this::toDomain);
    }

    @Override
    public void save(Transfer transfer) {
        springDataRepo.save(toEntity(transfer));
    }

    @Override
    public List<Transfer> findByOriginAccount(String accountNumber) {
        return springDataRepo.findByFromAccountNumber(accountNumber)
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByDestinationAccount(String accountNumber) {
        return springDataRepo.findByToAccountNumber(accountNumber)
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void update(Transfer transfer) {
        springDataRepo.save(toEntity(transfer));
    }

    @Override
    public boolean existsByTransferId(String transferId) {
        return springDataRepo.existsById(transferId);
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        return springDataRepo.findAll().stream()
            .filter(e -> e.getStatus().equals(status.toString()))
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
}
