package app.domain.services.commands;

import java.math.BigDecimal;

public class ApproveLoanCommand {
    private String loanId;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private String approverUserId;

    public ApproveLoanCommand(String loanId, BigDecimal approvedAmount,
                             BigDecimal interestRate, String approverUserId) {
        this.loanId = loanId;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.approverUserId = approverUserId;
    }

    public String getLoanId() { return loanId; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public String getApproverUserId() { return approverUserId; }
}
