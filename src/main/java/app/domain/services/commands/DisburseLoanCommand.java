package app.domain.services.commands;

public class DisburseLoanCommand {
    private String loanId;
    private String destinationAccountNumber;
    private String disburserUserId;

    public DisburseLoanCommand() {}

    public DisburseLoanCommand(String loanId, String destinationAccountNumber,
                              String disburserUserId) {
        this.loanId = loanId;
        this.destinationAccountNumber = destinationAccountNumber;
        this.disburserUserId = disburserUserId;
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }

    public String getDisburserUserId() { return disburserUserId; }
    public void setDisburserUserId(String disburserUserId) { this.disburserUserId = disburserUserId; }
}
