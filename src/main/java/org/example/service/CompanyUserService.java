package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.company.Company;
import org.example.entity.company.CompanyUser;
import org.example.entity.company.CompanyUserId;
import org.example.entity.user.User;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.CompanyRepository;
import org.example.repository.CompanyUserRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyUserService {

    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CompanyRepository companyRepository;

    /**
     * Lägger till en användare i ett företag baserat på e-post.
     * Perfekt för inbjudningar där man inte vet användarens UUID.
     */
    @Transactional
    public void addUserToCompanyByEmail(UUID companyId, String email) {
        log.info("Försöker lägga till användare med e-post {} till företag {}", email, companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Företag med ID " + companyId + " hittades inte"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Användare med e-post " + email + " hittades inte"));

        CompanyUserId id = new CompanyUserId(user.getId(), companyId);

        if (companyUserRepository.existsById(id)) {
            throw new BusinessRuleException("Användaren är redan kopplad till detta företag");
        }

        CompanyUser association = new CompanyUser(user, company);
        companyUserRepository.save(association);

        log.info("Användare {} tillagd i företag {}", user.getId(), companyId);
    }

    @Transactional
    public void addUserToCompanyById(UUID companyId, UUID userId) {
        log.info("Kopplar användare {} till företag {}", userId, companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Företag med ID " + companyId + " hittades inte"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Användare med ID " + userId + " hittades inte"));

        CompanyUserId id = new CompanyUserId(userId, companyId);

        if (companyUserRepository.existsById(id)) {
            throw new BusinessRuleException("Användaren är redan kopplad till detta företag");
        }

        companyUserRepository.save(new CompanyUser(user, company));
    }

    /**
     * Tar bort en användare från ett företag.
     */
    @Transactional
    public void deleteUserFromCompany(UUID companyId, UUID userId) {
        log.info("Tar bort användare {} från företag {}", userId, companyId);

        CompanyUserId id = new CompanyUserId(userId, companyId);

        if (!companyUserRepository.existsById(id)) {
            throw new EntityNotFoundException("Koppling mellan användare och företag hittades inte");
        }

        companyUserRepository.deleteById(id);
    }

    /**
     * Hämtar alla kopplingar för ett visst företag.
     */
    @Transactional(readOnly = true)
    public List<CompanyUser> getCompanyUsers(UUID companyId) {
        return companyUserRepository.findByCompanyId(companyId);
    }

    /**
     * Hämtar alla företag en viss användare tillhör.
     */
    @Transactional(readOnly = true)
    public List<CompanyUser> getUserCompanies(UUID userId) {
        return companyUserRepository.findByUserId(userId);
    }
}