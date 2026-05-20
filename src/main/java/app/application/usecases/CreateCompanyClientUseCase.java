package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.ports.UserRepository;
import app.domain.services.commands.CreateCompanyClientCommand;

@Component
public class CreateCompanyClientUseCase {
    private UserRepository userRepository;

    public CreateCompanyClientUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(CreateCompanyClientCommand command) {
        if (userRepository.existsById(command.getClientId())) {
            throw new IllegalArgumentException("Company client with ID " + command.getClientId() + " already exists");
        }
    }
}
