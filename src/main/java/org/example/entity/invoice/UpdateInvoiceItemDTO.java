package org.example.entity.invoice;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Används för att representera en rad i en faktura vid uppdatering.
 * Eftersom vi rensar och skapar om rader i din InvoiceService räcker det
 * oftast med att skicka med all data för raden på nytt.
 */
public record UpdateInvoiceItemDTO(
        @NotBlank(message = "Beskrivning saknas på raden")
        String name,

        @Min(value = 1, message = "Antal måste vara minst 1")
        int quantity,

        @NotNull(message = "Enhetspris måste anges")
        @DecimalMin(value = "0.0", message = "Priset kan inte vara negativt")
        BigDecimal unitPrice,

        @NotNull(message = "Momsbelopp måste anges")
        @DecimalMin(value = "0.0", message = "Momsen kan inte vara negativ")
        BigDecimal vatAmount
) {
}