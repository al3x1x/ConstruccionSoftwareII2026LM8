package app.domain.ports;

import app.domain.models.BankAccount;
import java.util.List;

public interface BankAccountRepository {
    BankAccount findByAccountNumber(String accountNumber);
    void save(BankAccount account);
    List<BankAccount> findByHolderId(String holderId);
    void update(BankAccount account);
    boolean existsByAccountNumber(String accountNumber);
}
