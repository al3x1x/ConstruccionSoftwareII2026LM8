package app.application.adapter.api.mappers;

import app.application.adapter.api.dto.UserResponseDTO;
import app.domain.models.User;

public final class UserMapper {
    private UserMapper() {}

    public static UserResponseDTO toDto(User u) {
        if (u == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(u.getUserId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setFullName(u.getFullName());
        dto.setIdentificationNumber(u.getIdentificationNumber());
        dto.setRole(u.getRole());
        dto.setStatus(u.getStatus());
        return dto;
    }
}
