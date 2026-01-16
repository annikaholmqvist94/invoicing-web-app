package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.client.Client;
import org.example.entity.company.Company;
import org.example.entity.invoice.*;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.ClientRepository;
import org.example.repository.CompanyRepository;
import org.example.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        log.info("Skapar faktura {} för företag {}", dto.number(), dto.companyId());

        if (invoiceRepository.findByNumber(dto.number()).isPresent()) {
            throw new BusinessRuleException("Fakturanummer " + dto.number() + " används redan.");
        }

        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Klienten hittades inte"));

        Invoice invoice = Invoice.builder()
                .number(dto.number())
                .company(company)
                .client(client)
                .dueDate(dto.dueDate())
                .status("DRAFT")
                .items(new ArrayList<>())
                .build();

        if (dto.items() != null) {
            dto.items().forEach(itemDto -> {
                InvoiceItem item = InvoiceItem.builder()
                        .name(itemDto.name())
                        .quantity(itemDto.quantity())
                        .unitPrice(itemDto.unitPrice())
                        .vatAmount(itemDto.vatAmount())
                        .invoice(invoice)
                        .build();
                invoice.addItem(item);
            });
        }

        invoice.recalculateTotals();
        return toDTO(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceDTO updateInvoice(UpdateInvoiceDTO dto) {
        log.info("Uppdaterar faktura med ID: {}", dto.invoiceId());

        Invoice invoice = invoiceRepository.findById(dto.invoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Fakturan hittades inte"));

        if (dto.dueDate() != null) invoice.setDueDate(dto.dueDate());
        if (dto.status() != null) invoice.setStatus(dto.status());

        if (dto.items() != null) {
            invoice.clearItems();
            dto.items().forEach(itemDto -> {
                InvoiceItem item = InvoiceItem.builder()
                        .name(itemDto.name())
                        .quantity(itemDto.quantity())
                        .unitPrice(itemDto.unitPrice())
                        .vatAmount(itemDto.vatAmount())
                        .invoice(invoice)
                        .build();
                invoice.addItem(item);
            });
        }

        invoice.recalculateTotals();
        return toDTO(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> getInvoiceById(UUID id) {
        return invoiceRepository.findById(id).map(this::toDTO);
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
        log.info("Faktura {} raderad", id);
    }

    /**
     * Hjälpmetod för att omvandla Invoice-entitet till InvoiceDTO.
     * Denna mappar även alla fakturarader (InvoiceItem) till InvoiceItemDTO.
     */
    private InvoiceDTO toDTO(Invoice entity) {
        List<InvoiceItemDTO> itemDTOs = entity.getItems().stream()
                .map(item -> InvoiceItemDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .vatAmount(item.getVatAmount())
                        .totalLineAmount(item.getTotalLineAmount())
                        .build())
                .toList();

        return InvoiceDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .dueDate(entity.getDueDate())
                .status(entity.getStatus())
                .totalNetAmount(entity.getTotalNetAmount())
                .totalVatAmount(entity.getTotalVatAmount())
                .totalGrossAmount(entity.getTotalGrossAmount())
                .items(itemDTOs)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}