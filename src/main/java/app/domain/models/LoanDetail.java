package app.domain.models;

import java.math.BigDecimal;

public class LoanDetail {

    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private String previousState;
    private String newState;
    private String analystId;

    // ── Constructor ───────────────────────────────────────────────────

    public LoanDetail(BigDecimal approvedAmount, BigDecimal interestRate,
                       String previousState, String newState, String analystId) {
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.previousState = previousState;
        this.newState = newState;
        this.analystId = analystId;
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public String getPreviousState() { return previousState; }
    public void setPreviousState(String previousState) { this.previousState = previousState; }

    public String getNewState() { return newState; }
    public void setNewState(String newState) { this.newState = newState; }

    public String getAnalystId() { return analystId; }
    public void setAnalystId(String analystId) { this.analystId = analystId; }

    @Override
    public String toString() {
        return "LoanDetail{approvedAmount=" + approvedAmount +
               ", interestRate=" + interestRate +
               ", previousState='" + previousState +
               "', newState='" + newState +
               "', analystId='" + analystId + "'}";
    }
}