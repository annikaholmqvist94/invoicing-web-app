package org.example.service;

import org.example.entity.Company;
import org.example.entity.User;
import org.example.repository.CompanyRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CompanyUserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public CompanyUserService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public void addUserToCompany(UUID userId, UUID companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Användare hittades inte"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Företag hittades inte"));

        user.getCompanies().add(company);
        userRepository.save(user); // JPA sköter automatiskt insättningen i user_company
    }
}