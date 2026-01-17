package org.example.entity.user;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        String email
) {

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

    }
}