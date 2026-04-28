package app.domain.services;

import app.domain.enums.TransferStatus;
import app.domain.models.Transfer;
import app.domain.ports.TransferRepository;
import app.domain.services.commands.CreateTransferCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CreateTransferService Tests")
public class CreateTransferServiceTest {
    private CreateTransferService service;
    private TransferRepository transferRepository;

    @BeforeEach
    void setUp() {
        transferRepository = mock(TransferRepository.class);
        service = new CreateTransferService(transferRepository);
    }

    @Test
    @DisplayName("Should create transfer with PENDING status when amount is below threshold")
    void testCreateTransferBelowThreshold() {
        // Arrange
        String transferId = "TRANS001";
        String originAccount = "ACC001";
        String destAccount = "ACC002";
        BigDecimal amount = new BigDecimal("5000.00"); // Below 10000 threshold

        CreateTransferCommand command = new CreateTransferCommand(
            transferId,
            originAccount,
            destAccount,
            amount,
            "USER001"
        );

        // Act
        Transfer result = service.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(transferId, result.getTransferId());
        assertEquals(originAccount, result.getOriginAccount());
        assertEquals(destAccount, result.getDestinationAccount());
        assertEquals(amount, result.getAmount());
        assertEquals(TransferStatus.PENDING, result.getStatus());
        verify(transferRepository, times(1)).save(result);
    }

    @Test
    @DisplayName("Should create transfer with AWAITING_APPROVAL status when amount is above threshold")
    void testCreateTransferAboveThreshold() {
        // Arrange
        String transferId = "TRANS001";
        String originAccount = "ACC001";
        String destAccount = "ACC002";
        BigDecimal amount = new BigDecimal("15000.00"); // Above 10000 threshold

        CreateTransferCommand command = new CreateTransferCommand(
            transferId,
            originAccount,
            destAccount,
            amount,
            "USER001"
        );

        // Act
        Transfer result = service.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(TransferStatus.AWAITING_APPROVAL, result.getStatus());
        verify(transferRepository, times(1)).save(result);
    }

    @Test
    @DisplayName("Should throw exception when origin and destination are the same")
    void testCreateTransferSameAccount() {
        // Arrange
        String transferId = "TRANS001";
        String sameAccount = "ACC001";
        BigDecimal amount = new BigDecimal("1000.00");

        CreateTransferCommand command = new CreateTransferCommand(
            transferId,
            sameAccount,
            sameAccount, // Same account
            amount,
            "USER001"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }
}
