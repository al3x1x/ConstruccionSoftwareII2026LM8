package app.domain.services.commands;

import java.math.BigDecimal;

public class ApproveLoanCommand {
    private String loanId;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private String approverUserId;

    public ApproveLoanCommand() {}

    public ApproveLoanCommand(String loanId, BigDecimal approvedAmount,
                             BigDecimal interestRate, String approverUserId) {
        this.loanId = loanId;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.approverUserId = approverUserId;
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public String getApproverUserId() { return approverUserId; }
    public void setApproverUserId(String approverUserId) { this.approverUserId = approverUserId; }
}
