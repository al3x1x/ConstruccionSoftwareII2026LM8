package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.LoanEntity;
import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import app.domain.enums.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface LoanRepositorySpringData extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByClientId(String clientId);
}

@Repository
class LoanRepositoryImpl implements LoanRepository {
    private final LoanRepositorySpringData springDataRepo;

    public LoanRepositoryImpl(LoanRepositorySpringData springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    private LoanEntity toEntity(Loan d) {
        LoanEntity e = new LoanEntity();
        e.setLoanId(d.getLoanId());
        e.setLoanType(d.getLoanType().toString());
        e.setClientId(d.getClientId());
        e.setRequestedAmount(d.getRequestedAmount());
        e.setApprovedAmount(d.getApprovedAmount());
        e.setInterestRate(d.getInterestRate());
        e.setTermMonths(d.getTermMonths());
        e.setApprovalDate(d.getApprovalDate());
        e.setDisbursementDate(d.getDisbursementDate());
        e.setDisbursementAccountNumber(d.getDisbursementAccountNumber());
        e.setAssignedCommercialEmployeeId(d.getAssignedCommercialEmployeeId());
        e.setStatus(d.getLoanStatus().toString());
        return e;
    }

    private Loan toDomain(LoanEntity e) {
        return new Loan(e.getLoanId(), LoanType.valueOf(e.getLoanType()), 
                       e.getClientId(), e.getRequestedAmount(), e.getTermMonths());
    }

    @Override
    public Optional<Loan> findByLoanId(String loanId) {
        return springDataRepo.findById(loanId).map(this::toDomain);
    }

    @Override
    public void save(Loan loan) {
        springDataRepo.save(toEntity(loan));
    }

    @Override
    public List<Loan> findByClientId(String clientId) {
        return springDataRepo.findByClientId(clientId)
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void update(Loan loan) {
        springDataRepo.save(toEntity(loan));
    }

    @Override
    public boolean existsByLoanId(String loanId) {
        return springDataRepo.existsById(loanId);
    }
}
