package app.application.adapter.persistence.mongodb;

import app.application.adapter.persistence.mongodb.documents.LoanDocument;
import app.application.adapter.persistence.mongodb.repositories.LoanMongoRepository;
import app.domain.models.Loan;
import app.domain.ports.LoanRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoDbLoanRepository implements LoanRepository {
    private final LoanMongoRepository mongoRepository;

    public MongoDbLoanRepository(LoanMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Loan findByLoanId(String loanId) {
        return mongoRepository.findById(loanId)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(Loan loan) {
        LoanDocument document = toDocument(loan);
        mongoRepository.save(document);
    }

    @Override
    public List<Loan> findByClientId(String clientId) {
        return mongoRepository.findByClientId(clientId)
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
        return mongoRepository.existsById(loanId);
    }

    private Loan toDomainModel(LoanDocument document) {
        Loan loan = new Loan(
            document.getLoanId(),
            document.getLoanType(),
            document.getClientId(),
            document.getRequestedAmount(),
            document.getDurationMonths()
        );
        loan.setApprovedAmount(document.getApprovedAmount());
        loan.setInterestRate(document.getInterestRate());
        loan.setLoanStatus(document.getStatus());
        loan.setApprovalDate(document.getApprovalDate());
        loan.setDisbursementDate(document.getDisbursementDate());
        return loan;
    }

    private LoanDocument toDocument(Loan loan) {
        LoanDocument document = new LoanDocument();
        document.setLoanId(loan.getLoanId());
        document.setClientId(loan.getClientId());
        document.setRequestedAmount(loan.getRequestedAmount());
        document.setApprovedAmount(loan.getApprovedAmount());
        document.setInterestRate(loan.getInterestRate());
        document.setLoanType(loan.getLoanType());
        document.setStatus(loan.getLoanStatus());
        document.setApprovalDate(loan.getApprovalDate());
        document.setDisbursementDate(loan.getDisbursementDate());
        document.setDurationMonths(loan.getTermMonths());
        document.setUpdatedAt(java.time.LocalDate.now());
        return document;
    }
}
