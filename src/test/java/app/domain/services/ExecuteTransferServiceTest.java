package app.domain.services;

import app.domain.enums.TransferStatus;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.ports.BankAccountRepository;
import app.domain.ports.TransferRepository;
import app.domain.services.commands.ExecuteTransferCommand;
import app.domain.exceptions.InvalidTransferException;
import app.domain.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ExecuteTransferService Tests")
public class ExecuteTransferServiceTest {
    private ExecuteTransferService service;
    private TransferRepository transferRepository;
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        transferRepository = mock(TransferRepository.class);
        bankAccountRepository = mock(BankAccountRepository.class);
        service = new ExecuteTransferService(transferRepository, bankAccountRepository);
    }

    @Test
    @DisplayName("Should execute transfer successfully")
    void testExecuteTransferSuccess() {
        // Arrange
        String transferId = "TRANS001";
        String originAccountNum = "ACC001";
        String destAccountNum = "ACC002";
        BigDecimal transferAmount = new BigDecimal("1000.00");

        Transfer transfer = new Transfer(transferId, originAccountNum, destAccountNum, transferAmount, "USER001");
        transfer.setStatus(TransferStatus.APPROVED); // Set to APPROVED

        BankAccount originAccount = new BankAccount(originAccountNum, null, "HOLDER001", "USD");
        originAccount.credit(new BigDecimal("5000.00")); // Add funds

        BankAccount destAccount = new BankAccount(destAccountNum, null, "HOLDER002", "USD");

        when(transferRepository.findByTransferId(transferId)).thenReturn(transfer);
        when(bankAccountRepository.findByAccountNumber(originAccountNum)).thenReturn(originAccount);
        when(bankAccountRepository.findByAccountNumber(destAccountNum)).thenReturn(destAccount);

        ExecuteTransferCommand command = new ExecuteTransferCommand(transferId, "EXECUTOR001");

        // Act
        service.execute(command);

        // Assert
        assertEquals(TransferStatus.EXECUTED, transfer.getStatus());
        assertEquals(new BigDecimal("4000.00"), originAccount.getCurrentBalance());
        assertEquals(new BigDecimal("1000.00"), destAccount.getCurrentBalance());
        verify(transferRepository, times(1)).update(transfer);
        verify(bankAccountRepository, times(2)).update(any(BankAccount.class));
    }

    @Test
    @DisplayName("Should throw exception when transfer not found")
    void testExecuteTransferNotFound() {
        // Arrange
        String transferId = "NONEXISTENT";
        when(transferRepository.findByTransferId(transferId)).thenReturn(null);

        ExecuteTransferCommand command = new ExecuteTransferCommand(transferId, "EXECUTOR001");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }

    @Test
    @DisplayName("Should throw exception when transfer has insufficient funds")
    void testExecuteTransferInsufficientFunds() {
        // Arrange
        String transferId = "TRANS001";
        String originAccountNum = "ACC001";
        String destAccountNum = "ACC002";
        BigDecimal transferAmount = new BigDecimal("5000.00");

        Transfer transfer = new Transfer(transferId, originAccountNum, destAccountNum, transferAmount, "USER001");
        transfer.setStatus(TransferStatus.PENDING);

        BankAccount originAccount = new BankAccount(originAccountNum, null, "HOLDER001", "USD");
        originAccount.credit(new BigDecimal("1000.00")); // Only 1000, need 5000

        BankAccount destAccount = new BankAccount(destAccountNum, null, "HOLDER002", "USD");

        when(transferRepository.findByTransferId(transferId)).thenReturn(transfer);
        when(bankAccountRepository.findByAccountNumber(originAccountNum)).thenReturn(originAccount);
        when(bankAccountRepository.findByAccountNumber(destAccountNum)).thenReturn(destAccount);

        ExecuteTransferCommand command = new ExecuteTransferCommand(transferId, "EXECUTOR001");

        // Act & Assert
        assertThrows(InsufficientFundsException.class, () -> service.execute(command));
    }

    @Test
    @DisplayName("Should throw exception when transfer is in invalid status")
    void testExecuteTransferInvalidStatus() {
        // Arrange
        String transferId = "TRANS001";
        Transfer transfer = new Transfer(transferId, "ACC001", "ACC002", new BigDecimal("1000.00"), "USER001");
        transfer.setStatus(TransferStatus.REJECTED); // Invalid status for execution

        when(transferRepository.findByTransferId(transferId)).thenReturn(transfer);

        ExecuteTransferCommand command = new ExecuteTransferCommand(transferId, "EXECUTOR001");

        // Act & Assert
        assertThrows(InvalidTransferException.class, () -> service.execute(command));
    }
}
