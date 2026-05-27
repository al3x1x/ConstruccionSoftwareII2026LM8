package app.application.usecases.users;

import app.application.adapter.api.dto.RegisterRequestDTO;
import app.application.adapter.api.dto.AuthResponseDTO;
import app.application.adapter.api.dto.LoginDTO;
import app.application.adapter.api.dto.UpdateUserDTO;
import app.application.adapter.api.dto.UserResponseDTO;
import app.application.adapter.api.mappers.UserMapper;
import app.domain.ports.UserRepository;
import app.domain.models.User;
import app.domain.models.UserFactory;
import app.domain.enums.UserStatus;
import app.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }

        User user = UserFactory.create(request.getRole());

        user.setUserId(UUID.randomUUID().toString());
        user.setFullName(request.getFullName());
        user.setIdentificationNumber(request.getIdentificationNumber());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate() != null ? request.getBirthDate() : java.time.LocalDate.of(1970, 1, 1));
        user.setAddress(request.getAddress());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public AuthResponseDTO authenticate(LoginDTO loginDTO) {
        User foundUser = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ERROR: El usuario NO existe en la BD"));
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), foundUser.getPasswordHash())) {
            throw new IllegalArgumentException("ERROR: La contraseña NO coincide con el hash");
        }
        if (!foundUser.isActive()) {
            throw new IllegalArgumentException("ERROR: El usuario está inactivo");
        }
        
        // CORRECCIÓN: Pasamos los 3 parámetros requeridos por tu JwtUtil
        String token = jwtUtil.generateToken(
            foundUser.getUsername(), 
            foundUser.getRole().toString(), 
            foundUser.getIdentificationNumber()
        );
        
        return new AuthResponseDTO(token, foundUser.getUsername(), foundUser.getRole().toString(), foundUser.getIdentificationNumber());
    }

    @Override
    @Transactional
    public AuthResponseDTO resetPassword(LoginDTO loginDTO) {
        return new AuthResponseDTO("", "", "", "");
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return List.of();
    }

    @Override
    public UserResponseDTO getById(String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return UserMapper.toDto(u);
    }

    @Override
    @Transactional
    public UserResponseDTO update(String id, UpdateUserDTO update) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return UserMapper.toDto(u);
    }

    @Override
    @Transactional
    public void delete(String id) {
    }
}