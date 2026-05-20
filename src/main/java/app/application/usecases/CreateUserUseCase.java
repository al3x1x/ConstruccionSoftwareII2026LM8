package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.User;
import app.domain.ports.UserRepository;
import app.domain.services.commands.CreateUserCommand;

@Component
public class CreateUserUseCase {
    private UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(CreateUserCommand command) {
        if (userRepository.existsById(command.getUserId())) {
            throw new IllegalArgumentException("User with ID " + command.getUserId() + " already exists");
        }

        return null;
    }
}
