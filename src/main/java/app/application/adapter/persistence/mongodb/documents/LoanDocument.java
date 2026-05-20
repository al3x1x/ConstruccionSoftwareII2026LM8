package app.application.adapter.persistence.mongodb.documents;

import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "loans")
public class LoanDocument {

    @Id
    private String loanId;

    private String clientId;
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private int durationMonths;
    private LoanStatus status;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String assignedCommercialEmployeeId;

    // Constructors
    public LoanDocument() {}

    public LoanDocument(String loanId, String clientId, LoanType loanType,
                       BigDecimal requestedAmount, int durationMonths) {
        this.loanId = loanId;
        this.clientId = clientId;
        this.loanType = loanType;
        this.requestedAmount = requestedAmount;
        this.durationMonths = durationMonths;
        this.status = LoanStatus.UNDER_REVIEW;
        this.createdAt = LocalDate.now();
    }

    // Getters and Setters
    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }

    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    public String getAssignedCommercialEmployeeId() { return assignedCommercialEmployeeId; }
    public void setAssignedCommercialEmployeeId(String assignedCommercialEmployeeId) {
        this.assignedCommercialEmployeeId = assignedCommercialEmployeeId;
    }
}
