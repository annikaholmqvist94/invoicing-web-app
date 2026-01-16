package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.company.Company;
import org.example.entity.company.CompanyDTO;
import org.example.entity.company.CreateCompanyDTO;
import org.example.entity.user.User;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CompanyRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional
    public CompanyDTO create(UUID creatorUserId, CreateCompanyDTO dto) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new EntityNotFoundException("Användare hittades inte"));

        if (companyRepository.existsByOrgNum((dto.orgNum()))){
            throw new BusinessRuleException("Organisationsnumret existerar redan");
        }

        Company company = Company.builder()
                .name(dto.name())
                .orgNum(dto.orgNum())
                .build();

        // Koppla användaren till företaget direkt via Setet
        company.getUsers().add(creator);
        creator.getCompanies().add(company);

        return CompanyDTO.fromEntity(companyRepository.save(company));
    }
}