package org.example.entity.client;

import java.util.UUID;

public record ClientDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        String city,
        String country
) {
    public static Object fromEntity(Client client) {
    }
}