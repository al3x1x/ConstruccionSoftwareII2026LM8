package app.application.adapter.persistence;

import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryLoanRepository implements LoanRepository {
    private Map<String, Loan> loans = new HashMap<>();

    @Override
    public Loan findByLoanId(String loanId) {
        return loans.get(loanId);
    }

    @Override
    public void save(Loan loan) {
        loans.put(loan.getLoanId(), loan);
    }

    @Override
    public List<Loan> findByClientId(String clientId) {
        return loans.values().stream()
            .filter(l -> clientId.equals(l.getClientId()))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Loan loan) {
        loans.put(loan.getLoanId(), loan);
    }

    @Override
    public boolean existsByLoanId(String loanId) {
        return loans.containsKey(loanId);
    }
}
