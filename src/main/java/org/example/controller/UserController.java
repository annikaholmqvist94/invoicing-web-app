package org.example.domain.user;

import org.example.dto.user.UserDTO;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        // I ett senare steg mappar vi om 'user' till 'UserDTO' här
        User savedUser = userService.registerUser(user);
        UserDTO response = new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail());
        return ResponseEntity.ok(response);
    }
}