package app.domain.ports;

import app.domain.models.Loan;
import java.util.List;

public interface LoanRepository {
    Loan findByLoanId(String loanId);
    void save(Loan loan);
    List<Loan> findByClientId(String clientId);
    void update(Loan loan);
    boolean existsByLoanId(String loanId);
}
