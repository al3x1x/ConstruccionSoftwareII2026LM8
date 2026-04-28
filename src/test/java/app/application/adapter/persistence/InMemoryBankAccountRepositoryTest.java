package app.application.adapter.persistence;

import app.domain.models.BankAccount;
import app.domain.enums.AccountType;
import app.domain.enums.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryBankAccountRepository Tests")
public class InMemoryBankAccountRepositoryTest {
    private InMemoryBankAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBankAccountRepository();
    }

    @Test
    @DisplayName("Should save and retrieve bank account")
    void testSaveAndRetrieve() {
        // Arrange
        BankAccount account = new BankAccount("ACC001", AccountType.SAVINGS, "HOLDER001", "USD");

        // Act
        repository.save(account);
        BankAccount retrieved = repository.findByAccountNumber("ACC001");

        // Assert
        assertNotNull(retrieved);
        assertEquals("ACC001", retrieved.getAccountNumber());
        assertEquals("HOLDER001", retrieved.getHolderId());
        assertEquals("USD", retrieved.getCurrency());
    }

    @Test
    @DisplayName("Should return null when account not found")
    void testFindNonExistent() {
        // Act
        BankAccount result = repository.findByAccountNumber("NONEXISTENT");

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should find accounts by holder ID")
    void testFindByHolderId() {
        // Arrange
        String holderId = "HOLDER001";
        BankAccount acc1 = new BankAccount("ACC001", AccountType.SAVINGS, holderId, "USD");
        BankAccount acc2 = new BankAccount("ACC002", AccountType.CHECKING, holderId, "USD");
        BankAccount acc3 = new BankAccount("ACC003", AccountType.SAVINGS, "OTHER_HOLDER", "USD");

        repository.save(acc1);
        repository.save(acc2);
        repository.save(acc3);

        // Act
        List<BankAccount> accounts = repository.findByHolderId(holderId);

        // Assert
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().allMatch(a -> holderId.equals(a.getHolderId())));
    }

    @Test
    @DisplayName("Should update bank account")
    void testUpdate() {
        // Arrange
        BankAccount account = new BankAccount("ACC001", AccountType.SAVINGS, "HOLDER001", "USD");
        repository.save(account);

        // Act - Credit the account
        account.credit(new BigDecimal("1000.00"));
        repository.update(account);

        // Assert
        BankAccount updated = repository.findByAccountNumber("ACC001");
        assertEquals(new BigDecimal("1000.00"), updated.getCurrentBalance());
    }

    @Test
    @DisplayName("Should check if account exists")
    void testExistsByAccountNumber() {
        // Arrange
        BankAccount account = new BankAccount("ACC001", AccountType.SAVINGS, "HOLDER001", "USD");
        repository.save(account);

        // Act & Assert
        assertTrue(repository.existsByAccountNumber("ACC001"));
        assertFalse(repository.existsByAccountNumber("NONEXISTENT"));
    }
}
