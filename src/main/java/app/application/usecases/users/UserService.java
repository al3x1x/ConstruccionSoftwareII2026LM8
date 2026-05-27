package app.application.usecases.users;

import app.application.adapter.api.dto.RegisterRequestDTO;
import app.application.adapter.api.dto.UpdateUserDTO;
import app.application.adapter.api.dto.UserResponseDTO;

import java.util.List;
import app.application.adapter.api.dto.AuthResponseDTO;
import app.application.adapter.api.dto.LoginDTO;

public interface UserService {
    UserResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO authenticate(LoginDTO loginDTO);
    AuthResponseDTO resetPassword(LoginDTO loginDTO);
    List<UserResponseDTO> getAll();
    UserResponseDTO getById(String id);
    UserResponseDTO update(String id, UpdateUserDTO update);
    void delete(String id);
}
