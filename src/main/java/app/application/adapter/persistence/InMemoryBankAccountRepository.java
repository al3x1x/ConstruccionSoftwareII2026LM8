package app.application.adapter.persistence;

import app.domain.models.BankAccount;
import app.domain.ports.BankAccountRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryBankAccountRepository implements BankAccountRepository {
    private Map<String, BankAccount> accounts = new HashMap<>();

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public void save(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public List<BankAccount> findByHolderId(String holderId) {
        return accounts.values().stream()
            .filter(a -> holderId.equals(a.getHolderId()))
            .collect(Collectors.toList());
    }

    @Override
    public void update(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}
