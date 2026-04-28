package app.domain.exceptions;

public class LoanAlreadyDisbursedException extends RuntimeException {
    public LoanAlreadyDisbursedException(String message) {
        super(message);
    }

    public LoanAlreadyDisbursedException(String message, Throwable cause) {
        super(message, cause);
    }
}
