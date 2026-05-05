package app.application.adapter.persistence.sqlserver;

import app.application.adapter.persistence.sqlserver.entities.BankAccountEntity;
import app.application.adapter.persistence.sqlserver.repositories.BankAccountJpaRepository;
import app.domain.models.BankAccount;
import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
import app.domain.ports.BankAccountRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlServerBankAccountRepository implements BankAccountRepository {
    private final BankAccountJpaRepository jpaRepository;

    public SqlServerBankAccountRepository(BankAccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return jpaRepository.findById(accountNumber)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(BankAccount account) {
        BankAccountEntity entity = toJpaEntity(account);
        jpaRepository.save(entity);
    }

    @Override
    public List<BankAccount> findByHolderId(String holderId) {
        return jpaRepository.findByHolderId(holderId)
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
        return jpaRepository.existsById(accountNumber);
    }

    private BankAccount toDomainModel(BankAccountEntity entity) {
        BankAccount account = new BankAccount(
            entity.getAccountNumber(),
            entity.getAccountType(),
            entity.getHolderId(),
            entity.getCurrency()
        );
        account.setStatus(entity.getStatus());
        account.setOpeningDate(entity.getOpeningDate());

        // Set balance through credit method to maintain domain logic
        if (entity.getCurrentBalance().signum() > 0) {
            account.credit(entity.getCurrentBalance());
        }

        return account;
    }

    private BankAccountEntity toJpaEntity(BankAccount account) {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setAccountNumber(account.getAccountNumber());
        entity.setHolderId(account.getHolderId());
        entity.setAccountType(account.getAccountType());
        entity.setCurrentBalance(account.getCurrentBalance());
        entity.setStatus(account.getStatus());
        entity.setCurrency(account.getCurrency());
        entity.setOpeningDate(account.getOpeningDate());
        entity.setUpdatedAt(java.time.LocalDate.now());
        return entity;
    }
}
