package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.entity.user.CreateUserDTO;
import org.example.entity.user.UserDTO;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // Lägg till denna för att slippa skriva konstruktorn manuellt
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserDTO dto) {
        // Vi mappar om DTO till en User i servicen eller här
        UserDTO response = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        // Authentication hämtas automatiskt från Spring Security (via din JWT-filter)
        String email = authentication.getName();
        UserDTO user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}