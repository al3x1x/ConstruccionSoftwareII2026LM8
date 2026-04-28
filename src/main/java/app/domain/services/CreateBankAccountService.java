package app.domain.services;

import app.domain.models.BankAccount;
import app.domain.models.User;
import app.domain.ports.BankAccountRepository;
import app.domain.ports.UserRepository;
import app.domain.ports.IdGenerator;
import app.domain.services.commands.CreateBankAccountCommand;

public class CreateBankAccountService {
    private UserRepository userRepository;
    private BankAccountRepository bankAccountRepository;
    private IdGenerator idGenerator;

    public CreateBankAccountService(UserRepository userRepository,
                                   BankAccountRepository bankAccountRepository,
                                   IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.idGenerator = idGenerator;
    }

    public BankAccount execute(CreateBankAccountCommand command) {
        // Validate that user exists
        User holder = userRepository.findById(command.getHolderId());
        if (holder == null) {
            throw new IllegalArgumentException("User with ID " + command.getHolderId() + " not found");
        }

        // Validate that account doesn't already exist
        if (bankAccountRepository.existsByAccountNumber(command.getAccountNumber())) {
            throw new IllegalArgumentException("Account with number " + command.getAccountNumber() + " already exists");
        }

        // Create and save account
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
