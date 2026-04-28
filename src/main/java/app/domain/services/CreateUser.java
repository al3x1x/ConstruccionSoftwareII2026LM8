package app.domain.services;

import app.domain.models.User;
import app.domain.ports.UserRepository;
import app.domain.services.commands.CreateUserCommand;

public class CreateUser {
    private UserRepository userRepository;

    public CreateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(CreateUserCommand command) {
        // Validate user doesn't already exist
        if (userRepository.existsById(command.getUserId())) {
            throw new IllegalArgumentException("User with ID " + command.getUserId() + " already exists");
        }

        // Note: User is abstract, so in practice this would need to create
        // specific subclasses based on role (NaturalPersonClient, TellerEmployee, etc.)
        // For now, this serves as the validation layer
        return null; // Placeholder - implementation depends on UserRole
    }
}
