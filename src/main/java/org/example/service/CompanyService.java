package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.company.*;
import org.example.entity.user.User;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CompanyRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional
    public CompanyDTO create(UUID creatorUserId, CreateCompanyRequest dto) {
        log.debug("Skapar företag: {} för användare: {}", dto.name(), creatorUserId);

        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new EntityNotFoundException("Användaren hittades inte"));

        if (companyRepository.existsByOrgNum(dto.orgNum())) {
            throw new BusinessRuleException("Ett företag med detta organisationsnummer finns redan");
        }

        // Skapa företagsobjektet (Entity)
        Company company = Company.builder()
                .name(dto.name())
                .orgNum(dto.orgNum())
                .build();

        // Om du använder en direkt koppling (ManyToMany) mellan User och Company:
        // company.getUsers().add(creator);

        Company savedCompany = companyRepository.save(company);
        log.info("Företag skapat med ID: {}", savedCompany.getId());

        return CompanyDTO.fromEntity(savedCompany);
    }

    @Transactional
    public CompanyDTO update(UpdateCompanyDTO dto) {
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));

        // Uppdatera fält om de finns i DTO:n
        if (dto.name() != null) company.setName(dto.name());
        if (dto.orgNum() != null) company.setOrgNum(dto.orgNum());
        if (dto.address() != null) company.setAddress(dto.address());
        if (dto.city() != null) company.setCity(dto.city());
        if (dto.country() != null) company.setCountry(dto.country());
        if (dto.phoneNumber() != null) company.setPhoneNumber(dto.phoneNumber());
        if (dto.email() != null) company.setEmail(dto.email());

        return CompanyDTO.fromEntity(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public Company getCompanyEntity(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));
    }

    @Transactional
    public void deleteCompany(UUID companyId) {
        log.warn("Tar bort företag med ID: {}", companyId);

        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Företaget hittades inte");
        }

        companyRepository.deleteById(companyId);
    }
}