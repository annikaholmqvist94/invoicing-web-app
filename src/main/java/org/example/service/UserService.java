package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.user.CreateUserDTO;
import org.example.entity.user.User;
import org.example.entity.user.UserDTO;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO registerUser(CreateUserDTO dto) {
        User user = User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        User savedUser = userRepository.save(user);


        return UserDTO.fromEntity(savedUser);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }


}