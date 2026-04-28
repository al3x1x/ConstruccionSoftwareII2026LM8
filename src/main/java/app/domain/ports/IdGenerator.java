package app.domain.ports;

public interface IdGenerator {
    String generateLoanId();
    String generateTransferId();
    String generateAccountNumber();
    String generateUserId();
}
