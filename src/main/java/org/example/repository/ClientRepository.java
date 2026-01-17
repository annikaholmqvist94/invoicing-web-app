package org.example.repository;

import org.example.entity.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    // Spring skapar automatiskt: "SELECT c FROM Client c WHERE c.company.id = :companyId"
    List<Client> findByCompanyId(UUID companyId);

    // Om du vill söka på e-post (användbart för validering)
    boolean existsByEmail(String email);
}