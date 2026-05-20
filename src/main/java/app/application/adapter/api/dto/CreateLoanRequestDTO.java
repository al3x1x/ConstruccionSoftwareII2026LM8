package app.application.adapter.api.dto;

import java.math.BigDecimal;

public class CreateLoanRequestDTO {
    private String loanType;
    private BigDecimal amount;
    private int termMonths;

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
}
