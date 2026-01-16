package org.example.entity.company;

import jakarta.validation.constraints.*;

/**
 * Används för att ta emot data när ett nytt företag skapas i systemet.
 */
public record CreateCompanyDTO(
        @NotBlank(message = "Organisationsnummer får inte vara tomt")
        @Pattern(regexp = "^[0-9]{6}[- ]?[0-9]{4}$", message = "Organisationsnummer måste vara i formatet XXXXXX-XXXX")
        String orgNum,

        @NotBlank(message = "Företagsnamn får inte vara tomt")
        @Size(max = 255, message = "Företagsnamnet får inte överstiga 255 tecken")
        String name,

        @NotBlank(message = "E-post får inte vara tom")
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