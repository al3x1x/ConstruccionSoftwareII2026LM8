package app.application.adapter.persistence.sqlserver.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class LoanEntity {
    @Id
    private String loanId;
    private String loanType;
    private String clientId;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private int termMonths;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementAccountNumber;
    private String assignedCommercialEmployeeId;
    private String status;

    public LoanEntity() {}

    public String getLoanId() { return loanId; }
    public void setLoanId(String v) { this.loanId = v; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String v) { this.loanType = v; }

    public String getClientId() { return clientId; }
    public void setClientId(String v) { this.clientId = v; }

    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal v) { this.requestedAmount = v; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal v) { this.approvedAmount = v; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal v) { this.interestRate = v; }

    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int v) { this.termMonths = v; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate v) { this.approvalDate = v; }

    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate v) { this.disbursementDate = v; }

    public String getDisbursementAccountNumber() { return disbursementAccountNumber; }
    public void setDisbursementAccountNumber(String v) { this.disbursementAccountNumber = v; }

    public String getAssignedCommercialEmployeeId() { return assignedCommercialEmployeeId; }
    public void setAssignedCommercialEmployeeId(String v) { this.assignedCommercialEmployeeId = v; }

    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}
