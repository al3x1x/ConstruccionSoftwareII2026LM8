package app.domain.services;

import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;
import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import app.domain.services.commands.ApproveLoanCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ApproveLoanService Tests")
public class ApproveLoanServiceTest {
    private ApproveLoanService service;
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        service = new ApproveLoanService(loanRepository);
    }

    @Test
    @DisplayName("Should approve loan successfully when loan is UNDER_REVIEW")
    void testApproveLoanSuccess() {
        // Arrange
        String loanId = "LOAN001";
        Loan loan = new Loan(loanId, LoanType.CONSUMER, "CLIENT001", new BigDecimal("5000.00"), 24);
        BigDecimal approvedAmount = new BigDecimal("4500.00");
        BigDecimal interestRate = new BigDecimal("0.05");

        when(loanRepository.findByLoanId(loanId)).thenReturn(loan);

        ApproveLoanCommand command = new ApproveLoanCommand(
            loanId,
            approvedAmount,
            interestRate,
            "ANALYST001"
        );

        // Act
        service.execute(command);

        // Assert
        assertEquals(LoanStatus.APPROVED, loan.getLoanStatus());
        assertEquals(approvedAmount, loan.getApprovedAmount());
        assertEquals(interestRate, loan.getInterestRate());
        verify(loanRepository, times(1)).update(loan);
    }

    @Test
    @DisplayName("Should throw exception when loan not found")
    void testApproveLoanNotFound() {
        // Arrange
        String loanId = "NONEXISTENT";
        when(loanRepository.findByLoanId(loanId)).thenReturn(null);

        ApproveLoanCommand command = new ApproveLoanCommand(
            loanId,
            new BigDecimal("4500.00"),
            new BigDecimal("0.05"),
            "ANALYST001"
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }

    @Test
    @DisplayName("Should throw exception when loan is not UNDER_REVIEW")
    void testApproveLoanInvalidStatus() {
        // Arrange
        String loanId = "LOAN001";
        Loan loan = new Loan(loanId, LoanType.CONSUMER, "CLIENT001", new BigDecimal("5000.00"), 24);
        loan.reject(); // Change status to REJECTED

        when(loanRepository.findByLoanId(loanId)).thenReturn(loan);

        ApproveLoanCommand command = new ApproveLoanCommand(
            loanId,
            new BigDecimal("4500.00"),
            new BigDecimal("0.05"),
            "ANALYST001"
        );

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> service.execute(command));
    }
}
