package app.domain.ports;

import app.domain.models.BankAccount;
import java.util.List;
import java.util.Optional; 

public interface BankAccountRepository {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    
    void save(BankAccount bankAccount);
    List<BankAccount> findByClientId(String clientId);
    void update(BankAccount bankAccount);
    boolean existsByAccountNumber(String accountNumber);
}