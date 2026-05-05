package app.application.adapter.persistence.mongodb;

import app.application.adapter.persistence.mongodb.documents.BankAccountDocument;
import app.application.adapter.persistence.mongodb.repositories.BankAccountMongoRepository;
import app.domain.models.BankAccount;
import app.domain.ports.BankAccountRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoDbBankAccountRepository implements BankAccountRepository {
    private final BankAccountMongoRepository mongoRepository;

    public MongoDbBankAccountRepository(BankAccountMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return mongoRepository.findById(accountNumber)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(BankAccount account) {
        BankAccountDocument document = toDocument(account);
        mongoRepository.save(document);
    }

    @Override
    public List<BankAccount> findByHolderId(String holderId) {
        return mongoRepository.findByHolderId(holderId)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void update(BankAccount account) {
        save(account);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return mongoRepository.existsById(accountNumber);
    }

    private BankAccount toDomainModel(BankAccountDocument document) {
        BankAccount account = new BankAccount(
            document.getAccountNumber(),
            document.getAccountType(),
            document.getHolderId(),
            document.getCurrency()
        );
        account.setStatus(document.getStatus());
        account.setOpeningDate(document.getOpeningDate());

        // Set balance through credit method to maintain domain logic
        if (document.getCurrentBalance().signum() > 0) {
            account.credit(document.getCurrentBalance());
        }

        return account;
    }

    private BankAccountDocument toDocument(BankAccount account) {
        BankAccountDocument document = new BankAccountDocument();
        document.setAccountNumber(account.getAccountNumber());
        document.setHolderId(account.getHolderId());
        document.setAccountType(account.getAccountType());
        document.setCurrentBalance(account.getCurrentBalance());
        document.setStatus(account.getStatus());
        document.setCurrency(account.getCurrency());
        document.setOpeningDate(account.getOpeningDate());
        document.setUpdatedAt(java.time.LocalDate.now());
        return document;
    }
}
