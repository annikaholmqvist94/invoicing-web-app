package org.example.service;

import org.example.entity.invoice.Invoice;
import org.example.repository.InvoiceItemRepository;
import org.example.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;


    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Invoice refreshInvoiceTotals(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Fakturan hittades inte"));

        // Kör beräkningen
        invoice.recalculateTotals();

        // Sparar de uppdaterade totalbeloppen
        return invoiceRepository.save(invoice);
    }

    // Exempel i InvoiceService
    public InvoiceDTO toDTO(Invoice entity) {
        List<InvoiceItemDTO> itemDTOs = entity.getItems().stream()
                .map(item -> new InvoiceItemDTO(
                        item.getId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getVatAmount(),
                        item.getTotalLineAmount() // Vår hjälpmetod från entiteten
                ))
                .toList();

        return new InvoiceDTO(
                entity.getId(),
                entity.getNumber(),
                entity.getDueDate(),
                entity.getStatus(),
                entity.getTotalNetAmount(),
                entity.getTotalVatAmount(),
                entity.getTotalGrossAmount(),
                itemDTOs
        );
    }
}