package app.domain.services.commands;

public class RejectLoanCommand {
    private String loanId;
    private String rejectorUserId;
    private String rejectionReason;

    public RejectLoanCommand() {}

    public RejectLoanCommand(String loanId, String rejectorUserId, String rejectionReason) {
        this.loanId = loanId;
        this.rejectorUserId = rejectorUserId;
        this.rejectionReason = rejectionReason;
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getRejectorUserId() { return rejectorUserId; }
    public void setRejectorUserId(String rejectorUserId) { this.rejectorUserId = rejectorUserId; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
