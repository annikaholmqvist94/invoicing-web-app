package org.example.entity.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// I UpdateInvoiceDTO.java
public record UpdateInvoiceDTO(
        @NotNull UUID invoiceId,
        LocalDateTime dueDate,
        String status,
        List<UpdateInvoiceItemDTO> items // Denna refererar nu till din nya Record
) {}