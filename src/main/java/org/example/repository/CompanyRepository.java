package org.example.repository;

import org.example.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    /**
     * Spring skapar automatiskt SQL-frågan:
     * SELECT COUNT(*) FROM companies WHERE org_num = ?
     */
    boolean existsByOrgNum(String orgNum);
}