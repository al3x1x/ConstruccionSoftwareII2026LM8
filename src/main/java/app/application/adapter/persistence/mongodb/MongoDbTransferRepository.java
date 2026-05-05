package app.application.adapter.persistence.mongodb;

import app.application.adapter.persistence.mongodb.documents.TransferDocument;
import app.application.adapter.persistence.mongodb.repositories.TransferMongoRepository;
import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoDbTransferRepository implements TransferRepository {
    private final TransferMongoRepository mongoRepository;

    public MongoDbTransferRepository(TransferMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Transfer findByTransferId(String transferId) {
        return mongoRepository.findById(transferId)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(Transfer transfer) {
        TransferDocument document = toDocument(transfer);
        mongoRepository.save(document);
    }

    @Override
    public List<Transfer> findByOriginAccount(String accountNumber) {
        return mongoRepository.findByOriginAccount(accountNumber)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByDestinationAccount(String accountNumber) {
        return mongoRepository.findByDestinationAccount(accountNumber)
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
        return mongoRepository.existsById(transferId);
    }

    private Transfer toDomainModel(TransferDocument document) {
        Transfer transfer = new Transfer(
            document.getTransferId(),
            document.getOriginAccount(),
            document.getDestinationAccount(),
            document.getAmount(),
            document.getCreatorUserId()
        );
        transfer.setStatus(document.getStatus());
        transfer.setCreationDate(document.getCreationDate());
        transfer.setApprovalDate(document.getApprovalDate());
        transfer.setApproverUserId(document.getApproverUserId());
        return transfer;
    }

    private TransferDocument toDocument(Transfer transfer) {
        TransferDocument document = new TransferDocument();
        document.setTransferId(transfer.getTransferId());
        document.setOriginAccount(transfer.getOriginAccount());
        document.setDestinationAccount(transfer.getDestinationAccount());
        document.setAmount(transfer.getAmount());
        document.setStatus(transfer.getStatus());
        document.setCreationDate(transfer.getCreationDate());
        document.setApprovalDate(transfer.getApprovalDate());
        document.setCreatorUserId(transfer.getCreatorUserId());
        document.setApproverUserId(transfer.getApproverUserId());
        document.setUpdatedAt(java.time.LocalDateTime.now());
        return document;
    }
}
