package org.example.entity.invoice;

import java.math.BigDecimal;
import java.util.UUID;



public record InvoiceItemDTO(
        UUID id,
        String name, Integer quantity,
        BigDecimal unitPrice,
        BigDecimal vatAmount,
        BigDecimal lineTotal // (Quantity * Price) + VatAmount
) {}