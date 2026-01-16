package org.example.entity.client;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record UpdateClientDTO(
        @NotNull(message = "Klient-ID krävs för uppdatering")
        UUID clientId,

        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Email(message = "Ange en giltig e-postadress")
        String email,

        String address,
        String country,
        String city,
        String phoneNumber
) {}