package org.example.entity.client;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record CreateClientDTO(
        @NotNull(message = "Företags-ID krävs")
        UUID companyId,

        @NotBlank(message = "Förnamn får inte vara tomt")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "Efternamn får inte vara tomt")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "E-post får inte vara tom")
        @Email(message = "Ange en giltig e-postadress")
        @Size(max = 255)
        String email,

        @Size(max = 255)
        String address,

        @Size(max = 100)
        String country,

        @Size(max = 100)
        String city,

        @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Telefonnumret innehåller ogiltiga tecken")
        String phoneNumber
) {}