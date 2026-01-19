package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.invoice.CreateInvoiceDTO;
import org.example.entity.invoice.InvoiceDTO;
import org.example.entity.invoice.UpdateInvoiceDTO;
import org.example.security.SecurityService;
import org.example.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor // Skapar automatiskt konstruktorn för dina final services
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final SecurityService securityService;

    /**
     * Skapar en ny faktura med manuellt angivna momsbelopp per rad.
     */
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody CreateInvoiceDTO dto) {
        securityService.verifyCompanyAccess(dto.companyId());

        InvoiceDTO createdInvoice = invoiceService.createInvoice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    /**
     * Hämtar en specifik faktura inklusive alla rader och beräknade totaler.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Hämtar alla fakturor som tillhör ett specifikt företag.
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByCompany(@PathVariable UUID companyId) {
        securityService.verifyCompanyAccess(companyId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        log.info("Användare {} försöker hämta fakturor för företag {}", currentUserEmail, companyId);

        // Här bör du i en perfekt värld kontrollera att currentUserEmail faktiskt
        // har tillgång till companyId innan du anropar servicen.
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByCompany(companyId);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Uppdaterar en faktura (t.ex. ändrar rader, moms eller status).
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInvoiceDTO dto) {

        // Säkerställ att ID i URL matchar ID i bodyn om det behövs
        if (!id.equals(dto.invoiceId())) {
            return ResponseEntity.badRequest().build();
        }

        InvoiceDTO updated = invoiceService.updateInvoice(dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Raderar en faktura (om den inte är markerad som betald).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}