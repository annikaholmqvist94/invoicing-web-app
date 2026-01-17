package org.example.entity.client;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientDTO(
        UUID id,
        UUID companyId,
        String firstName,
        String lastName,
        String email,
        String address,
        String country,
        String city,
        String phoneNumber,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ClientDTO fromEntity(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getCompany().getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getAddress(),
                client.getCountry(),
                client.getCity(),
                client.getPhoneNumber(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }
}