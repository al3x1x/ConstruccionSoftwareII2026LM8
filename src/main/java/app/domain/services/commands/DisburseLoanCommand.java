package app.domain.services.commands;

public class DisburseLoanCommand {
    private String loanId;
    private String destinationAccountNumber;
    private String disburserUserId;

    public DisburseLoanCommand(String loanId, String destinationAccountNumber,
                              String disburserUserId) {
        this.loanId = loanId;
        this.destinationAccountNumber = destinationAccountNumber;
        this.disburserUserId = disburserUserId;
    }

    public String getLoanId() { return loanId; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public String getDisburserUserId() { return disburserUserId; }
}
