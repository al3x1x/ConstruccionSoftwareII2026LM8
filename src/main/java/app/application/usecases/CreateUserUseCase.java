package app.application.usecases;

import org.springframework.stereotype.Component;
import app.domain.models.User;
import app.domain.models.UserFactory;
import app.domain.ports.UserRepository;
import app.domain.services.commands.CreateUserCommand;

@Component
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(CreateUserCommand command) {
        // 1. Validar si ya existe el usuario por ID
        if (userRepository.existsById(command.getUserId())) {
            throw new IllegalArgumentException("User with ID " + command.getUserId() + " already exists");
        }

        // 2. Crear la subclase de usuario correspondiente usando la fábrica y el rol del command
        User user = UserFactory.create(command.getRole());

        // 3. Mapear únicamente los campos básicos que sí existen en tu CreateUserCommand original
        user.setUserId(command.getUserId());
        user.setFullName(command.getFullName());
        user.setEmail(command.getEmail());
        user.setUsername(command.getUsername());
        user.setPasswordHash(command.getPasswordHash());
        user.setRole(command.getRole());

        // 4. Guardar (Como tu puerto retorna void, lo llamamos solo)
        userRepository.save(user);
        
        // 5. Retornar el objeto user que acabamos de armar
        return user;
    }
}