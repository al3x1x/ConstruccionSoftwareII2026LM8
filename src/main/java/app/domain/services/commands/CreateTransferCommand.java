package app.domain.services.commands;

import java.math.BigDecimal;

public class CreateTransferCommand {
    private String transferId;
    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private String creatorUserId;

    public CreateTransferCommand(String transferId, String originAccount,
                                String destinationAccount, BigDecimal amount,
                                String creatorUserId) {
        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
    }

    public String getTransferId() { return transferId; }
    public String getOriginAccount() { return originAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getCreatorUserId() { return creatorUserId; }
}
