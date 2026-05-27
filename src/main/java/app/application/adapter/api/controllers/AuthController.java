package app.application.adapter.api.controllers;

import app.application.adapter.api.dto.RegisterRequestDTO;
import app.application.adapter.api.dto.UserResponseDTO;
import app.application.adapter.api.dto.LoginDTO;
import app.application.adapter.api.dto.AuthResponseDTO;
import app.application.usecases.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Asegúrate de que tenga ESTA anotación exacta
@RequestMapping("/api/auth") // Mapea la base de la URL
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register") // Engancha directamente con /api/auth/register
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        UserResponseDTO response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login") // Para tu otro endpoint obligatorio
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        AuthResponseDTO response = userService.authenticate(loginDTO);
        return ResponseEntity.ok(response);
    }
}