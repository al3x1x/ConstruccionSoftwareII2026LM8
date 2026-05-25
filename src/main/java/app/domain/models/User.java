package app.domain.models;

import app.domain.enums.UserRole;
import app.domain.enums.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    private String userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String identificationNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
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