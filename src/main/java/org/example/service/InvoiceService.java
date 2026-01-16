package org.example.service;

import org.example.entity.invoice.Invoice;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
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
    public void deleteInvoice(UUID id) {
        Invoice invoice = InvoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fakturan finns inte"));

        if ("PAID".equals(invoice.getStatus())) {
            throw new BusinessRuleException("Du kan inte radera en faktura som redan är markerad som betald!");
        }

        InvoiceRepository.delete(invoice);
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