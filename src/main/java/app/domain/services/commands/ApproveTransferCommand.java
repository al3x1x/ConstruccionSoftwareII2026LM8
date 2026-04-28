package app.domain.services.commands;

public class ApproveTransferCommand {
    private String transferId;
    private String approverUserId;

    public ApproveTransferCommand(String transferId, String approverUserId) {
        this.transferId = transferId;
        this.approverUserId = approverUserId;
    }

    public String getTransferId() { return transferId; }
    public String getApproverUserId() { return approverUserId; }
}
