package app.application.adapter.api.dto;

import java.math.BigDecimal;

public class CreateTransferDTO {
    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;

    public String getOriginAccount() { return originAccount; }
    public void setOriginAccount(String originAccount) { this.originAccount = originAccount; }

    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
