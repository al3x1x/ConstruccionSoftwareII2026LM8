package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.User;
import app.domain.models.NaturalPersonClient;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.ports.UserRepository;
import app.domain.ports.BankAccountRepository;
import app.domain.ports.LoanRepository;
import app.domain.exceptions.UnauthorizedClientAccessException;
import java.util.List;

@Component
public class NaturalClientUseCase {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final LoanRepository loanRepository;

    public NaturalClientUseCase(UserRepository userRepository,
                                BankAccountRepository bankAccountRepository,
                                LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.loanRepository = loanRepository;
    }

    private void validateEmployeeClientRelationship(String employeeId, String clientId) {
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no existe: " + clientId));

        if (!(client instanceof NaturalPersonClient)) {
            throw new IllegalArgumentException("Usuario no es cliente natural");
        }

        NaturalPersonClient naturalClient = (NaturalPersonClient) client;
        if (!employeeId.equals(naturalClient.getAssignedCommercialEmployeeId())) {
            throw new UnauthorizedClientAccessException(employeeId, clientId);
        }
    }

    public NaturalPersonClient getClientDetails(String employeeId, String clientId) {
        validateEmployeeClientRelationship(employeeId, clientId);
        return (NaturalPersonClient) userRepository.findById(clientId).get();
    }

    public List<Loan> listClientLoans(String employeeId, String clientId) {
        validateEmployeeClientRelationship(employeeId, clientId);
        return loanRepository.findByClientId(clientId);
    }

    public List<BankAccount> listClientAccounts(String employeeId, String clientId) {
        validateEmployeeClientRelationship(employeeId, clientId);
        NaturalPersonClient client = (NaturalPersonClient) userRepository.findById(clientId).get();
        return client.getAccounts();
    }
}
