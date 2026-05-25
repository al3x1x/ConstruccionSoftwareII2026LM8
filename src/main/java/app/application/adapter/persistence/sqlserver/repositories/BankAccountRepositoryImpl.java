package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.BankAccountEntity;
import app.domain.models.BankAccount;
import app.domain.ports.BankAccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface BankAccountRepositorySpringData extends JpaRepository<BankAccountEntity, String> {
    Optional<BankAccountEntity> findByAccountNumber(String accountNumber);
    List<BankAccountEntity> findByHolderId(String holderId);
    boolean existsByAccountNumber(String accountNumber);
}

@Repository
@Primary
class BankAccountRepositoryImpl implements BankAccountRepository {

    private final BankAccountRepositorySpringData springDataRepo;

    public BankAccountRepositoryImpl(BankAccountRepositorySpringData springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    private BankAccountEntity toEntity(BankAccount d) {
        BankAccountEntity e = new BankAccountEntity();
        e.setAccountNumber(d.getAccountNumber());
        e.setAccountType(d.getAccountType());
        e.setHolderId(d.getHolderId());
        e.setCurrentBalance(d.getCurrentBalance());
        e.setCurrency(d.getCurrency());
        e.setStatus(d.getStatus());
        e.setOpeningDate(d.getOpeningDate());
        return e;
    }

    private BankAccount toDomain(BankAccountEntity e) {
        BankAccount account = new BankAccount(
            e.getAccountNumber(),
            e.getAccountType(),
            e.getHolderId(),
            e.getCurrency()
        );
        account.setStatus(e.getStatus());
        account.setOpeningDate(e.getOpeningDate());
        account.setCurrentBalance(e.getCurrentBalance());
        return account;
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return springDataRepo.findByAccountNumber(accountNumber).map(this::toDomain);
    }

    @Override
    public void save(BankAccount bankAccount) {
        springDataRepo.save(toEntity(bankAccount));
    }

    @Override
    public List<BankAccount> findByClientId(String clientId) {
        return springDataRepo.findByHolderId(clientId)
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void update(BankAccount bankAccount) {
        springDataRepo.save(toEntity(bankAccount));
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return springDataRepo.existsByAccountNumber(accountNumber);
    }
}