package org.example.controller;

import jakarta.validation.Valid;
import org.example.entity.company.Company;
import org.example.entity.company.CompanyDTO;
import org.example.entity.company.CreateCompanyDTO;
import org.example.entity.user.User;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CompanyRepository;
import org.example.repository.UserRepository;
import org.example.service.CompanyService;
import org.example.service.CompanyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(
            @Valid @RequestBody CreateCompanyDTO createDto,
            Authentication authentication) {

        // Hämta e-post från JWT (Säkerställer att rätt person blir ägare)
        String userEmail = authentication.getName();
        return ResponseEntity.ok(companyService.createCompanyByEmail(createDto, userEmail));
    }

    @PostMapping("/{companyId}/users/{userId}")
    public ResponseEntity<Void> addUserToCompany(
            @PathVariable UUID companyId,
            @PathVariable UUID userId) {
        companyUserService.addUserToCompanyById(companyId, userId);
        return ResponseEntity.ok().build();
    }
}