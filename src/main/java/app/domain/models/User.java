package app.domain.models;

import app.domain.enums.UserRole;
import app.domain.enums.UserStatus;

import java.time.LocalDate;

public abstract class User {

    private String userId;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private UserRole role;
    private UserStatus status;
    private String username;
    private String passwordHash;


    public User(String userId, String fullName, String identificationNumber,
                String email, String phone, LocalDate birthDate,
                String address, UserRole role, String username, String passwordHash) {

        this.userId = userId;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.username = username;
        this.passwordHash = passwordHash;
        this.status = UserStatus.ACTIVE; // todo usuario empieza activo
    }


    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }


    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", fullName='" + fullName + "', role=" + role + ", status=" + status + "}";
    }
}