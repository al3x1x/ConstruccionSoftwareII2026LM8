package app.domain.models;

import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Loan {

    private String loanId;
    private LoanType loanType;
    private String clientId;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private int termMonths;
    private LoanStatus loanStatus;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementAccountNumber;
    private String assignedCommercialEmployeeId;

    // ── Constructor ────────────────────────────────────────────────────

    public Loan(String loanId, LoanType loanType, String clientId,
                BigDecimal requestedAmount, int termMonths) {
        this.loanId = loanId;
        this.loanType = loanType;
        this.clientId = clientId;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.loanStatus = LoanStatus.UNDER_REVIEW; // all loans start in UNDER_REVIEW status
    }

    // ── Business Methods ──────────────────────────────────────────────────

    public void approve(BigDecimal approvedAmount, BigDecimal interestRate) {
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.loanStatus = LoanStatus.APPROVED;
        this.approvalDate = LocalDate.now();
    }

    public void reject() {
        this.loanStatus = LoanStatus.REJECTED;
    }

    public void disburse(BankAccount destinationAccount) {
        destinationAccount.credit(this.approvedAmount);
        this.disbursementAccountNumber = destinationAccount.getAccountNumber();
        this.disbursementDate = LocalDate.now();
        this.loanStatus = LoanStatus.DISBURSED;
    }

    // ── Getters & Setters ──────────────────────────────────────────────

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

    public String getAssignedCommercialEmployeeId() { return assignedCommercialEmployeeId; }
    public void setAssignedCommercialEmployeeId(String assignedCommercialEmployeeId) {
        this.assignedCommercialEmployeeId = assignedCommercialEmployeeId;
    }

    @Override
    public String toString() {
        return "Loan{loanId='" + loanId + "', clientId='" + clientId +
               "', status=" + loanStatus + ", requested=" + requestedAmount + "}";
    }
}