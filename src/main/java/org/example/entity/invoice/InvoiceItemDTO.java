package org.example.entity.invoice;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO för att visa data om en fakturarad.
 * Innehåller nu stöd för manuell moms och beräknade totaler för vyn.
 */
@Builder
public record InvoiceItemDTO(
        UUID id,

        @NotBlank(message = "Beskrivning saknas på raden")
        String name,

        @Min(value = 1, message = "Antal måste vara minst 1")
        int quantity,

        @NotNull(message = "Enhetspris måste anges")
        @DecimalMin(value = "0.0", message = "Priset kan inte vara negativt")
        BigDecimal unitPrice,

        @NotNull(message = "Momsbelopp måste anges")
        @DecimalMin(value = "0.0", message = "Momsen kan inte vara negativ")
        BigDecimal vatAmount,

        BigDecimal totalLineAmount // Netto + Moms för denna rad
) {
    /**
     * Mappar en InvoiceItem-entitet till en InvoiceItemDTO.
     */
    public static InvoiceItemDTO fromEntity(InvoiceItem item) {
        if (item == null) return null;

        return InvoiceItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .vatAmount(item.getVatAmount()) // Hämtar det manuella momsbeloppet från entiteten
                .totalLineAmount(item.getTotalLineAmount()) // Hämtar (Pris * Antal) + Moms
                .build();
    }
}