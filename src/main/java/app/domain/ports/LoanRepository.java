package app.domain.ports;

import app.domain.models.Loan;
import java.util.List;
import java.util.Optional; 

public interface LoanRepository {
    Optional<Loan> findByLoanId(String loanId);
    
    void save(Loan loan);
    List<Loan> findByClientId(String clientId);
    void update(Loan loan);
    boolean existsByLoanId(String loanId);
}