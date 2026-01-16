package org.example.repository;

import org.example.entity.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    // Sök på fakturanummer (ersätter findByInvoiceNumber)
    Optional<Invoice> findByNumber(String number);

    // Hämtar fakturan och alla dess rader i EN SQL-fråga (JOIN FETCH)
    // Detta är kritiskt för prestanda i Azure för att undvika "N+1"-problem
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.items WHERE i.id = :id")
    Optional<Invoice> findByIdWithItems(@Param("id") UUID id);

    // Hitta alla fakturor för ett specifikt företag inkl. rader
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.items WHERE i.company.id = :companyId")
    List<Invoice> findByCompanyId(@Param("companyId") UUID companyId);

    // Hitta alla fakturor för en specifik klient inkl. rader
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.items WHERE i.client.id = :clientId")
    List<Invoice> findByClientId(@Param("clientId") UUID clientId);

    // Hitta fakturor baserat på status och företag
    List<Invoice> findByStatusAndCompanyId(String status, UUID companyId);

    // Kontrollera om ett fakturanummer redan existerar
    boolean existsByNumber(String number);
}