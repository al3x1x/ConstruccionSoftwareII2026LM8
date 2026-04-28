package app.domain.services;

import app.domain.enums.AccountType;
import app.domain.models.BankAccount;
import app.domain.models.User;
import app.domain.ports.BankAccountRepository;
import app.domain.ports.UserRepository;
import app.domain.ports.IdGenerator;
import app.domain.services.commands.CreateBankAccountCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CreateBankAccountService Tests")
public class CreateBankAccountServiceTest {
    private CreateBankAccountService service;
    private UserRepository userRepository;
    private BankAccountRepository bankAccountRepository;
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        bankAccountRepository = mock(BankAccountRepository.class);
        idGenerator = mock(IdGenerator.class);
        service = new CreateBankAccountService(userRepository, bankAccountRepository, idGenerator);
    }

    @Test
    @DisplayName("Should create bank account successfully when user exists")
    void testCreateBankAccountSuccess() {
        // Arrange
        String userId = "USER123";
        String accountNumber = "ACC001";
        User mockUser = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(mockUser);
        when(bankAccountRepository.existsByAccountNumber(accountNumber)).thenReturn(false);

        CreateBankAccountCommand command = new CreateBankAccountCommand(
            accountNumber,
            AccountType.SAVINGS,
            userId,
            "USD"
        );

        // Act
        BankAccount result = service.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals(userId, result.getHolderId());
        assertEquals("USD", result.getCurrency());
        verify(bankAccountRepository, times(1)).save(result);
    }

    @Test
    @DisplayName("Should throw exception when user does not exist")
    void testCreateBankAccountUserNotFound() {
        // Arrange
        String userId = "NONEXISTENT";
        when(userRepository.findById(userId)).thenReturn(null);

        CreateBankAccountCommand command = new CreateBankAccountCommand(
            "ACC001",
            AccountType.SAVINGS,
            userId,
            "USD"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }

    @Test
    @DisplayName("Should throw exception when account already exists")
    void testCreateBankAccountAlreadyExists() {
        // Arrange
        String userId = "USER123";
        String accountNumber = "ACC001";
        User mockUser = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(mockUser);
        when(bankAccountRepository.existsByAccountNumber(accountNumber)).thenReturn(true);

        CreateBankAccountCommand command = new CreateBankAccountCommand(
            accountNumber,
            AccountType.SAVINGS,
            userId,
            "USD"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }
}
