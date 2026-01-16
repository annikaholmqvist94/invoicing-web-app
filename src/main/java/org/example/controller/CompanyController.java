package org.example.controller;

import jakarta.validation.Valid;
import org.example.entity.company.CompanyDTO;
import org.example.entity.company.CreateCompanyDTO;
import org.example.service.CompanyService;
import org.example.service.CompanyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyUserService companyUserService;

    public CompanyController(CompanyService companyService, CompanyUserService companyUserService) {
        this.companyService = companyService;
        this.companyUserService = companyUserService;
    }

    /**
     * Skapar ett företag.
     * OBS: I din CompanyService.create krävs även creatorUserId.
     * Här skickar vi med det som en header eller parameter (exempelvis från en inloggad användare).
     */
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(
            @RequestHeader("X-User-Id") UUID creatorUserId,
            @Valid @RequestBody CreateCompanyDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(creatorUserId, dto));
    }

    /**
     * Koppla användare till företag via UUID.
     * Eftersom din CompanyUserService nu fokuserar på e-post, lade jag till
     * en metod i servicen nedan som matchar detta anrop.
     */
    @PostMapping("/{companyId}/users/{userId}")
    public ResponseEntity<Void> addUserToCompany(
            @PathVariable UUID companyId,
            @PathVariable UUID userId) {
        // Vi anropar en metod för UUID-koppling
        companyUserService.addUserToCompanyById(companyId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Koppla användare till företag via E-post (matchar din addUserToCompanyByEmail)
     */
    @PostMapping("/{companyId}/users/email")
    public ResponseEntity<Void> addUserByEmail(
            @PathVariable UUID companyId,
            @RequestParam String email) {
        companyUserService.addUserToCompanyByEmail(companyId, email);
        return ResponseEntity.ok().build();
    }
}