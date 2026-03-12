package app.domain.models;

public class TransferDetail {

    private double amount;
    private double balanceBeforeOrigin;
    private double balanceAfterOrigin;
    private double balanceBeforeDestination;
    private double balanceAfterDestination;

    // ── Constructor ───────────────────────────────────────────────────

    public TransferDetail(double amount, double balanceBeforeOrigin,
                           double balanceAfterOrigin, double balanceBeforeDestination,
                           double balanceAfterDestination) {
        this.amount = amount;
        this.balanceBeforeOrigin = balanceBeforeOrigin;
        this.balanceAfterOrigin = balanceAfterOrigin;
        this.balanceBeforeDestination = balanceBeforeDestination;
        this.balanceAfterDestination = balanceAfterDestination;
    }


    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getBalanceBeforeOrigin() { return balanceBeforeOrigin; }
    public void setBalanceBeforeOrigin(double balanceBeforeOrigin) { this.balanceBeforeOrigin = balanceBeforeOrigin; }

    public double getBalanceAfterOrigin() { return balanceAfterOrigin; }
    public void setBalanceAfterOrigin(double balanceAfterOrigin) { this.balanceAfterOrigin = balanceAfterOrigin; }

    public double getBalanceBeforeDestination() { return balanceBeforeDestination; }
    public void setBalanceBeforeDestination(double balanceBeforeDestination) { this.balanceBeforeDestination = balanceBeforeDestination; }

    public double getBalanceAfterDestination() { return balanceAfterDestination; }
    public void setBalanceAfterDestination(double balanceAfterDestination) { this.balanceAfterDestination = balanceAfterDestination; }

    @Override
    public String toString() {
        return "TransferDetail{amount=" + amount +
               ", balanceBeforeOrigin=" + balanceBeforeOrigin +
               ", balanceAfterOrigin=" + balanceAfterOrigin +
               ", balanceBeforeDestination=" + balanceBeforeDestination +
               ", balanceAfterDestination=" + balanceAfterDestination + "}";
    }
}