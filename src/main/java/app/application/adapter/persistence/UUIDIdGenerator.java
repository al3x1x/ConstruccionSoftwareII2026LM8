package app.application.adapter.persistence;

import app.domain.ports.IdGenerator;
import java.util.UUID;

public class UUIDIdGenerator implements IdGenerator {

    @Override
    public String generateLoanId() {
        return "LOAN-" + UUID.randomUUID().toString();
    }

    @Override
    public String generateTransferId() {
        return "TRANS-" + UUID.randomUUID().toString();
    }

    @Override
    public String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString();
    }

    @Override
    public String generateUserId() {
        return "USER-" + UUID.randomUUID().toString();
    }
}
