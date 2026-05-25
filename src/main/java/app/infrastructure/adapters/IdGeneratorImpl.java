package app.infrastructure.adapters;

import app.domain.ports.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorImpl implements IdGenerator {

    @Override
    public String generateLoanId() {
        return "LOAN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String generateTransferId() {
        return "TRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    @Override
    public String generateUserId() {
        return "USER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
