package org.example.domain.company;

import org.example.entity.Company;
import org.example.service.CompanyService;
import org.example.service.CompanyUserService;
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

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }

    // Koppla användare till företag: POST /api/companies/{companyId}/users/{userId}
    @PostMapping("/{companyId}/users/{userId}")
    public ResponseEntity<Void> addUserToCompany(
            @PathVariable UUID companyId,
            @PathVariable UUID userId) {
        companyUserService.addUserToCompany(userId, companyId);
        return ResponseEntity.ok().build();
    }
}