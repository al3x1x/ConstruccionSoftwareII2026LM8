package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.BankAccount;
import app.domain.models.User;
import app.domain.ports.BankAccountRepository;
import app.domain.ports.UserRepository;
import app.domain.ports.IdGenerator;
import app.domain.services.commands.CreateBankAccountCommand;

@Component
public class CreateBankAccountUseCase {
    private UserRepository userRepository;
    private BankAccountRepository bankAccountRepository;
    private IdGenerator idGenerator;

    public CreateBankAccountUseCase(UserRepository userRepository,
                                   BankAccountRepository bankAccountRepository,
                                   IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.idGenerator = idGenerator;
    }

    public BankAccount execute(CreateBankAccountCommand command) {
        User holder = userRepository.findById(command.getHolderId())
            .orElseThrow(() -> new IllegalArgumentException("User with ID " + command.getHolderId() + " not found"));

        if (bankAccountRepository.existsByAccountNumber(command.getAccountNumber())) {
            throw new IllegalArgumentException("Account with number " + command.getAccountNumber() + " already exists");
        }

        BankAccount account = new BankAccount(
            command.getAccountNumber(),
            command.getAccountType(),
            command.getHolderId(),
            command.getCurrency()
        );

        bankAccountRepository.save(account);
        return account;
    }
}
