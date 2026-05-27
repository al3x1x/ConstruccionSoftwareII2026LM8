package app.application.adapter.api.dto;

public class AuthResponseDTO {
    private String token;
    private String username;
    private String role;
    private String identificationNumber;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public AuthResponseDTO(String token, String username, String role, String identificationNumber) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.identificationNumber = identificationNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
}
