package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.user.LoginRequest;
import org.example.service.JwtService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("Inloggningsförsök för: " + request.email()); // DEBUG

        return userService.findEntityByEmail(request.email())
                .map(user -> {
                    boolean matches = passwordEncoder.matches(request.password(), user.getPassword());
                    System.out.println("Lösenord matchar: " + matches); // DEBUG
                    if (matches) {
                        String token = jwtService.generateToken(user.getEmail());
                        return ResponseEntity.ok(Map.of("token", token));
                    }
                    return ResponseEntity.status(401).build();
                })
                .orElse(ResponseEntity.status(401).build());
    }

}