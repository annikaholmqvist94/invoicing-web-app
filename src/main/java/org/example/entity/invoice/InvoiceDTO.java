package org.example.entity.invoice;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record InvoiceDTO(
        UUID id,
        UUID companyId,
        UUID clientId,
        String number,
        String status,
        BigDecimal totalNetAmount,
        BigDecimal totalVatAmount,
        BigDecimal totalGrossAmount,
        LocalDateTime dueDate,
        LocalDateTime createdAt,
        List<InvoiceItemDTO> items
) {
    /**
     * Mappar om en Invoice-entitet till en InvoiceDTO.
     * Denna version använder de nya summeringsfälten för tydlighet.
     */
    public static InvoiceDTO fromEntity(Invoice invoice) {
        if (invoice == null) return null;

        List<InvoiceItemDTO> itemDTOs = invoice.getItems() != null
                ? invoice.getItems().stream()
                .map(InvoiceItemDTO::fromEntity)
                .toList()
                : List.of();

        return InvoiceDTO.builder()
                .id(invoice.getId())
                .companyId(invoice.getCompany().getId())
                .clientId(invoice.getClient().getId())
                .number(invoice.getNumber())
                .status(invoice.getStatus())
                .totalNetAmount(invoice.getTotalNetAmount())
                .totalVatAmount(invoice.getTotalVatAmount())
                .totalGrossAmount(invoice.getTotalGrossAmount())
                .dueDate(invoice.getDueDate())
                .createdAt(invoice.getCreatedAt())
                .items(itemDTOs)
                .build();
    }
}