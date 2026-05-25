package app.application.adapter.persistence.sqlserver.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String userId;
    private String username;
    private String email;
    private String password;

    public UserEntity() {}

    public String getUserId() { return userId; }
    public void setUserId(String v) { this.userId = v; }

    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getPassword() { return password; }
    public void setPassword(String v) { this.password = v; }
}
