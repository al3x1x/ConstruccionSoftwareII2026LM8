package app.domain.services.commands;

public class ExecuteTransferCommand {
    private String transferId;
    private String executorUserId;

    public ExecuteTransferCommand() {}

    public ExecuteTransferCommand(String transferId, String executorUserId) {
        this.transferId = transferId;
        this.executorUserId = executorUserId;
    }

    public String getTransferId() { return transferId; }
    public void setTransferId(String transferId) { this.transferId = transferId; }

    public String getExecutorUserId() { return executorUserId; }
    public void setExecutorUserId(String executorUserId) { this.executorUserId = executorUserId; }
}
