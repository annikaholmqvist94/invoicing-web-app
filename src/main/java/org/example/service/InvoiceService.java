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

        // Validera att fakturanumret är unikt (från din gamla logik)
        if (invoiceRepository.findByNumber(dto.number()).isPresent()) {
            throw new BusinessRuleException("Fakturanummer " + dto.number() + " används redan.");
        }

        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Klienten hittades inte"));

        // Bygg fakturan med Builder (istället för fromDTO för bättre kontroll)
        Invoice invoice = Invoice.builder()
                .number(dto.number())
                .company(company)
                .client(client)
                .dueDate(dto.dueDate())
                .status("DRAFT")
                .items(new ArrayList<>())
                .build();

        // Mappa rader med manuell vatAmount
        if (dto.items() != null) {
            dto.items().forEach(itemDto -> {
                InvoiceItem item = InvoiceItem.builder()
                        .name(itemDto.name())
                        .quantity(itemDto.quantity())
                        .unitPrice(itemDto.unitPrice())
                        .vatAmount(itemDto.vatAmount())
                        .invoice(invoice)
                        .build();
                invoice.addItem(item); // Använd addItem för att trigga omdanande av totalsummor
            });
        }

        invoice.recalculateTotals();
        return InvoiceDTO.fromEntity(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceDTO updateInvoice(UpdateInvoiceDTO dto) {
        log.info("Uppdaterar faktura med ID: {}", dto.invoiceId());

        Invoice invoice = invoiceRepository.findById(dto.invoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Fakturan hittades inte"));

        // Uppdatera grundfält
        if (dto.dueDate() != null) invoice.setDueDate(dto.dueDate());
        if (dto.status() != null) invoice.setStatus(dto.status());

        // Om rader skickas med, ersätt de gamla (från din gamla logik)
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
        return InvoiceDTO.fromEntity(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> getInvoiceById(UUID id) {
        return invoiceRepository.findById(id)
                .map(InvoiceDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByCompany(UUID companyId) {
        return invoiceRepository.findByCompanyId(companyId).stream()
                .map(InvoiceDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fakturan hittades inte"));

        // Säkerhetsregel från din gamla logik
        if ("PAID".equals(invoice.getStatus())) {
            throw new BusinessRuleException("Betalda fakturor kan inte raderas");
        }

        invoiceRepository.delete(invoice);
        log.info("Faktura {} raderad", id);
    }
}