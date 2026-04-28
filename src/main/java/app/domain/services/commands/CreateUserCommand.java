package app.domain.services.commands;

import app.domain.enums.UserRole;

public class CreateUserCommand {
    private String userId;
    private String name;
    private String email;
    private UserRole role;

    public CreateUserCommand(String userId, String name, String email, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
}
