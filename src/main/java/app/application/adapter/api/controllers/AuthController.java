package app.application.adapter.api.controllers;

import app.application.adapter.api.dto.AuthResponseDTO;
import app.application.adapter.api.dto.LoginDTO;
import app.domain.models.User;
import app.domain.ports.UserRepository;
import app.infrastructure.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> user = userRepository.findByUsername(loginDTO.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Usuario o contraseña inválidos"));
        }

        User foundUser = user.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), foundUser.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Usuario o contraseña inválidos"));
        }

        if (!foundUser.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Usuario inactivo"));
        }

        String token = jwtUtil.generateToken(
            foundUser.getIdentificationNumber(),
            foundUser.getUsername(),
            foundUser.getRole()
        );

        return ResponseEntity.ok(new AuthResponseDTO(token, foundUser.getUsername(), foundUser.getRole().toString()));
    }

    private static class ErrorResponse {
        public int status;
        public String message;

        public ErrorResponse(String message) {
            this.status = 401;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
