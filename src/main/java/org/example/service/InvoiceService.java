package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.client.Client;
import org.example.entity.company.Company;
import org.example.entity.invoice.*;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CompanyRepository;
import org.example.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public InvoiceDTO createInvoice(CreateInvoiceDTO dto) {
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företag hittades inte"));

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Klient hittades inte"));

        Invoice invoice = Invoice.builder()
                .number(dto.number())
                .company(company)
                .client(client)
                .status("DRAFT")
                .items(new ArrayList<>())
                .build();

        // Mappa rader
        if (dto.items() != null) {
            dto.items().forEach(itemDto -> {
                InvoiceItem item = InvoiceItem.builder()
                        .name(itemDto.name())
                        .quantity(itemDto.quantity())
                        .unitPrice(itemDto.unitPrice())
                        .invoice(invoice)
                        .build();
                invoice.getItems().add(item);
            });
        }

        invoice.recalculateTotals();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return toDTO(savedInvoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByCompany(UUID companyId) {
        return invoiceRepository.findByCompanyId(companyId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public void deleteById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fakturan hittades inte"));

        if ("PAID".equals(invoice.getStatus())) {
            throw new BusinessRuleException("Betalda fakturor kan inte raderas");
        }

        invoiceRepository.delete(invoice);
    }

    // Hjälpmetod för att omvandla Entity till DTO
    public InvoiceDTO toDTO(Invoice entity) {
        List<InvoiceItemDTO> itemDTOs = entity.getItems().stream()
                .map(item -> new InvoiceItemDTO(
                        item.getId(),
                        item.getName(), // Lade till name om det behövs i din Record
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getVatAmount(),
                        item.getTotalLineAmount()
                )).toList();

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