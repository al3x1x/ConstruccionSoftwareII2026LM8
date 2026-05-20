package app.application.adapter.persistence.mongodb.documents;

import app.domain.enums.UserRole;
import app.domain.enums.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String name;
    private String username;
    private String email;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private String passwordHash;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String assignedCommercialEmployeeId;

    // Constructors
    public UserDocument() {}

    public UserDocument(String id, String name, String email, String phone, UserRole role, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.passwordHash = passwordHash;
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDate.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    public String getAssignedCommercialEmployeeId() { return assignedCommercialEmployeeId; }
    public void setAssignedCommercialEmployeeId(String assignedCommercialEmployeeId) {
        this.assignedCommercialEmployeeId = assignedCommercialEmployeeId;
    }
}

