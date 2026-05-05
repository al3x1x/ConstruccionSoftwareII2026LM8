package app.application.adapter.persistence.sqlserver;

import app.application.adapter.persistence.sqlserver.entities.LoanEntity;
import app.application.adapter.persistence.sqlserver.repositories.LoanJpaRepository;
import app.domain.models.Loan;
import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;
import app.domain.ports.LoanRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlServerLoanRepository implements LoanRepository {
    private final LoanJpaRepository jpaRepository;

    public SqlServerLoanRepository(LoanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Loan findByLoanId(String loanId) {
        return jpaRepository.findById(loanId)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(Loan loan) {
        LoanEntity entity = toJpaEntity(loan);
        jpaRepository.save(entity);
    }

    @Override
    public List<Loan> findByClientId(String clientId) {
        return jpaRepository.findByClientId(clientId)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Loan loan) {
        save(loan);
    }

    @Override
    public boolean existsByLoanId(String loanId) {
        return jpaRepository.existsById(loanId);
    }

    private Loan toDomainModel(LoanEntity entity) {
        Loan loan = new Loan(
            entity.getLoanId(),
            entity.getLoanType(),
            entity.getClientId(),
            entity.getRequestedAmount(),
            entity.getTermMonths()
        );
        loan.setApprovedAmount(entity.getApprovedAmount());
        loan.setInterestRate(entity.getInterestRate());
        loan.setLoanStatus(entity.getLoanStatus());
        loan.setApprovalDate(entity.getApprovalDate());
        loan.setDisbursementDate(entity.getDisbursementDate());
        loan.setDisbursementAccountNumber(entity.getDisbursementAccountNumber());
        return loan;
    }

    private LoanEntity toJpaEntity(Loan loan) {
        LoanEntity entity = new LoanEntity();
        entity.setLoanId(loan.getLoanId());
        entity.setLoanType(loan.getLoanType());
        entity.setClientId(loan.getClientId());
        entity.setRequestedAmount(loan.getRequestedAmount());
        entity.setApprovedAmount(loan.getApprovedAmount());
        entity.setInterestRate(loan.getInterestRate());
        entity.setTermMonths(loan.getTermMonths());
        entity.setLoanStatus(loan.getLoanStatus());
        entity.setApprovalDate(loan.getApprovalDate());
        entity.setDisbursementDate(loan.getDisbursementDate());
        entity.setDisbursementAccountNumber(loan.getDisbursementAccountNumber());
        entity.setUpdatedAt(java.time.LocalDate.now());
        return entity;
    }
}
