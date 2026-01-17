package org.example.entity.company;

import jakarta.validation.constraints.*;
import java.util.UUID;

/**
 * Används för att uppdatera ett befintligt företag i Azure SQL.
 */
public record UpdateCompanyDTO(
        @NotNull(message = "Företags-ID får inte vara tomt")
        UUID companyId,

        @Size(max = 255, message = "Företagsnamnet får inte överstiga 255 tecken")
        String name,

        @Size(max = 50, message = "Organisationsnummer får inte överstiga 50 tecken")
        String orgNum,

        @Email(message = "Ange en giltig e-postadress")
        @Size(max = 255)
        String email,

        @Size(max = 255)
        String address,

        @Size(max = 100)
        String city,

        @Size(max = 100)
        String country,

        @Size(max = 50)
        @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Telefonnumret innehåller ogiltiga tecken")
        String phoneNumber
) {
}