package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.repository.CompanyUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final CompanyUserRepository companyUserRepository;

    public void verifyCompanyAccess(UUID companyId) {
        // 1. Hämta e-post från JWT-token via SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Kolla i databasen om denna användare är kopplad till detta företag
        boolean hasAccess = companyUserRepository.existsByUser_EmailAndCompany_Id(email, companyId);

        if (!hasAccess) {
            throw new org.example.exception.AuthorizationException(
                    "Access Denied: You do not belong to company " + companyId
            );
        }
    }
}