package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.company.*;
import org.example.entity.user.User;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CompanyRepository;
import org.example.repository.UserRepository;
import org.example.repository.CompanyUserRepository; // Behövs för kopplingen
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

    /**
     * Skapar ett nytt företag och kopplar skaparen till företaget.
     */
    @Transactional
    public CompanyDTO create(UUID creatorUserId, CreateCompanyDTO dto) {
        log.info("Startar skapande av företag: {} (OrgNr: {})", dto.name(), dto.orgNum());

        // 1. Kontrollera om orgNum redan finns (från gamla logiken)
        if (companyRepository.existsByOrgNum(dto.orgNum())) {
            log.warn("Kunde inte skapa företag: OrgNum {} finns redan", dto.orgNum());
            throw new BusinessRuleException("Företag med detta organisationsnummer existerar redan");
        }

        // 2. Hämta skaparen
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new EntityNotFoundException("Användaren som skapar företaget hittades inte"));

        // 3. Bygg företaget
        Company company = Company.builder()
                .name(dto.name())
                .orgNum(dto.orgNum())
                .email(dto.email())
                .address(dto.address())
                .city(dto.city())
                .country(dto.country())
                .phoneNumber(dto.phoneNumber())
                .build();

        // 4. Spara företaget i Azure SQL
        Company savedCompany = companyRepository.save(company);

        // 5. Skapa kopplingen i CompanyUser (från gamla logiken)
        CompanyUser association = new CompanyUser(creator, savedCompany);
        companyUserRepository.save(association);

        log.info("Företag skapat med ID: {} och kopplat till användare: {}", savedCompany.getId(), creatorUserId);
        return CompanyDTO.fromEntity(savedCompany);
    }

    @Transactional
    public CompanyDTO update(UpdateCompanyDTO dto) {
        log.info("Uppdaterar företag med ID: {}", dto.companyId());

        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));

        // Använd entitetens egna update-metod om den finns, annars sätt fält här
        company.updateFromDTO(dto);

        return CompanyDTO.fromEntity(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public Company getCompanyEntity(UUID companyId) {
        log.debug("Hämtar företag entitet för ID: {}", companyId);
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));
    }

    @Transactional
    public void deleteCompany(UUID companyId) {
        log.warn("Försöker ta bort företag med ID: {}", companyId);

        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Företaget hittades inte");
        }

        // JPA sköter borttagning av rader i company_user automatiskt om du har CascadeType.ALL
        companyRepository.deleteById(companyId);
        log.info("Företag med ID: {} raderat", companyId);
    }


}