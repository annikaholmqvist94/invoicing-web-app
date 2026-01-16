package org.example.entity.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



public record InvoiceItemDTO(
        UUID id,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal vatAmount,
        BigDecimal lineTotal // (Quantity * Price) + VatAmount
) {}