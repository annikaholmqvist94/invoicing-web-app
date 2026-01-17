package org.example.entity.invoice;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateInvoiceItemDTO(
        @NotBlank(message = "Beskrivning saknas på raden")
        String name,

        @Min(value = 1, message = "Antal måste vara minst 1")
        int quantity,

        @NotNull(message = "Enhetspris måste anges")
        @DecimalMin(value = "0.0", message = "Priset kan inte vara negativt")
        BigDecimal unitPrice, // Glöm inte kommatecknet här!

        @NotNull(message = "Momsbelopp måste anges")
        @DecimalMin(value = "0.0", message = "Momsen kan inte vara negativ")
        BigDecimal vatAmount
) {
}