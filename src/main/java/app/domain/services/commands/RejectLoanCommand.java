package app.domain.services.commands;

public class RejectLoanCommand {
    private String loanId;
    private String rejectorUserId;
    private String rejectionReason;

    public RejectLoanCommand(String loanId, String rejectorUserId, String rejectionReason) {
        this.loanId = loanId;
        this.rejectorUserId = rejectorUserId;
        this.rejectionReason = rejectionReason;
    }

    public String getLoanId() { return loanId; }
    public String getRejectorUserId() { return rejectorUserId; }
    public String getRejectionReason() { return rejectionReason; }
}
