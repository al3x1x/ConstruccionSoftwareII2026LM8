package app.domain.services.commands;

import app.domain.enums.UserRole;

import java.time.LocalDate;

public class CreateUserCommand {

    private String userId;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String username;
    private String passwordHash;
    private UserRole role;

    public CreateUserCommand(String userId, String fullName, String identificationNumber,
                             String email, String phone, LocalDate birthDate,
                             String address, String username, String passwordHash, UserRole role) {

        this.userId = userId;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }
}