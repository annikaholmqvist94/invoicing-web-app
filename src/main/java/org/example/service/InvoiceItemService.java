package org.example.service;

import org.example.entity.invoice.InvoiceItem;
import org.example.repository.InvoiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class InvoiceItemService {

    private final InvoiceItemRepository itemRepository;

    public InvoiceItemService(InvoiceItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public InvoiceItem updateItem(UUID itemId, Integer quantity, BigDecimal unitPrice, double vatRate) {
        InvoiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Raden hittades inte"));

        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);

        // Beräkna momsbeloppet (Pris * Antal * Momsats)
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal calculatedVat = lineTotal.multiply(BigDecimal.valueOf(vatRate));
        item.setVatAmount(calculatedVat);

        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(UUID itemId) {
        itemRepository.deleteById(itemId);
    }
}