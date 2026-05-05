package app.application.adapter.persistence.mongodb.documents;

import app.domain.enums.AuditOperationType;
import app.domain.enums.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class AuditLogDocument {

    @Id
    private String id;

    private String userId;
    private UserRole userRole;
    private AuditOperationType operationType;
    private String entityType;
    private String entityId;
    private String description;
    private LocalDateTime timestamp;
    private String ipAddress;
    private LocalDate createdAt;

    // Constructors
    public AuditLogDocument() {}

    public AuditLogDocument(String userId, AuditOperationType operationType, String entityType,
                           String entityId, String description) {
        this.userId = userId;
        this.operationType = operationType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.createdAt = LocalDate.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

    public AuditOperationType getOperationType() { return operationType; }
    public void setOperationType(AuditOperationType operationType) { this.operationType = operationType; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
