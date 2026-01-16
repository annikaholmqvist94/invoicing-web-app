package org.example.entity.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceDTO(
        UUID id,
        String number,
        LocalDateTime dueDate,
        String status,
        BigDecimal totalNetAmount,
        BigDecimal totalVatAmount,
        BigDecimal totalGrossAmount,
        List<org.example.entity.invoice.InvoiceItemDTO> items
) {
    public static InvoiceDTO fromEntity(Invoice save) {
    }
}