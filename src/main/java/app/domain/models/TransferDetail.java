package app.domain.models;

import java.math.BigDecimal;

public class TransferDetail {

    private BigDecimal amount;
    private BigDecimal balanceBeforeOrigin;
    private BigDecimal balanceAfterOrigin;
    private BigDecimal balanceBeforeDestination;
    private BigDecimal balanceAfterDestination;

    // ── Constructor ───────────────────────────────────────────────────

    public TransferDetail(BigDecimal amount, BigDecimal balanceBeforeOrigin,
                           BigDecimal balanceAfterOrigin, BigDecimal balanceBeforeDestination,
                           BigDecimal balanceAfterDestination) {
        this.amount = amount;
        this.balanceBeforeOrigin = balanceBeforeOrigin;
        this.balanceAfterOrigin = balanceAfterOrigin;
        this.balanceBeforeDestination = balanceBeforeDestination;
        this.balanceAfterDestination = balanceAfterDestination;
    }


    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getBalanceBeforeOrigin() { return balanceBeforeOrigin; }
    public void setBalanceBeforeOrigin(BigDecimal balanceBeforeOrigin) { this.balanceBeforeOrigin = balanceBeforeOrigin; }

    public BigDecimal getBalanceAfterOrigin() { return balanceAfterOrigin; }
    public void setBalanceAfterOrigin(BigDecimal balanceAfterOrigin) { this.balanceAfterOrigin = balanceAfterOrigin; }

    public BigDecimal getBalanceBeforeDestination() { return balanceBeforeDestination; }
    public void setBalanceBeforeDestination(BigDecimal balanceBeforeDestination) { this.balanceBeforeDestination = balanceBeforeDestination; }

    public BigDecimal getBalanceAfterDestination() { return balanceAfterDestination; }
    public void setBalanceAfterDestination(BigDecimal balanceAfterDestination) { this.balanceAfterDestination = balanceAfterDestination; }

    @Override
    public String toString() {
        return "TransferDetail{amount=" + amount +
               ", balanceBeforeOrigin=" + balanceBeforeOrigin +
               ", balanceAfterOrigin=" + balanceAfterOrigin +
               ", balanceBeforeDestination=" + balanceBeforeDestination +
               ", balanceAfterDestination=" + balanceAfterDestination + "}";
    }
}