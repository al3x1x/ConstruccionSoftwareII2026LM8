package app.application.adapter.persistence.sqlserver.entities;

import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class LoanEntity {

    @Id
    @Column(name = "loan_id")
    private String loanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "term_months", nullable = false)
    private int termMonths;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatus loanStatus;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "disbursement_account_number")
    private String disbursementAccountNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    // Constructors
    public LoanEntity() {}

    public LoanEntity(String loanId, LoanType loanType, String clientId,
                     BigDecimal requestedAmount, int termMonths) {
        this.loanId = loanId;
        this.loanType = loanType;
        this.clientId = clientId;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.loanStatus = LoanStatus.UNDER_REVIEW;
        this.createdAt = LocalDate.now();
    }

    // Getters and Setters
    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }

    public LoanStatus getLoanStatus() { return loanStatus; }
    public void setLoanStatus(LoanStatus loanStatus) { this.loanStatus = loanStatus; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }

    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }

    public String getDisbursementAccountNumber() { return disbursementAccountNumber; }
    public void setDisbursementAccountNumber(String disbursementAccountNumber) { this.disbursementAccountNumber = disbursementAccountNumber; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
