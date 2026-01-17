package org.example.repository;

import org.example.entity.company.CompanyUser;
import org.example.entity.company.CompanyUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, CompanyUserId> {
    List<CompanyUser> findByCompanyId(UUID companyId);
    List<CompanyUser> findByUserId(UUID userId);

    boolean existsByUser_EmailAndCompany_Id(String email, UUID companyId);
}