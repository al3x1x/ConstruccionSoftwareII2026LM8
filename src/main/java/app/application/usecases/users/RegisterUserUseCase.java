package app.application.usecases.users;

import app.domain.models.User;
import app.domain.services.commands.CreateUserCommand;

public interface RegisterUserUseCase {
    User execute(CreateUserCommand command);
}