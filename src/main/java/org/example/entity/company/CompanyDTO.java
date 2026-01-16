package org.example.dto.company;

import java.util.UUID;

public record CompanyDTO(
        UUID id,
        String name,
        String orgNum,
        String email,
        String phoneNumber,
        String address,
        String city,
        String country
) {}

