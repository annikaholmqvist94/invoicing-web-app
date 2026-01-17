package org.example.entity.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for user registration.
 */
public record CreateUserDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {}