package org.example.entity.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateInvoiceDTO(
        @NotNull(message = "Företags-ID får inte vara tomt")
        UUID companyId,

        @NotNull(message = "Klient-ID får inte vara tomt")
        UUID clientId,

        @NotBlank(message = "Fakturanummer måste anges")
        @Size(min = 1, max = 50, message = "Fakturanummer måste vara mellan 1 och 50 tecken")
        String number,

        @NotNull(message = "Förfallodatum måste anges")
        @Future(message = "Förfallodatumet måste vara i framtiden")
        LocalDateTime dueDate,

        @NotNull(message = "Fakturarader får inte saknas")
        @NotEmpty(message = "Fakturan måste innehålla minst en rad")
        @Valid
        List<CreateInvoiceItemDTO> items
) {
}