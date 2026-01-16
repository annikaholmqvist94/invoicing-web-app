package org.example.entity.invoice;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.company.Company;
import org.example.entity.client.Client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String number;

    private String status; // Detta fält saknades och behövs för getStatus()

    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    private BigDecimal totalNetAmount;
    private BigDecimal totalVatAmount;
    private BigDecimal totalGrossAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceItem> items;

    public void recalculateTotals() {
        if (items == null || items.isEmpty()) {
            this.totalNetAmount = BigDecimal.ZERO;
            this.totalVatAmount = BigDecimal.ZERO;
            this.totalGrossAmount = BigDecimal.ZERO;
            return;
        }

        this.totalNetAmount = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalVatAmount = items.stream()
                .map(item -> item.getVatAmount() != null ? item.getVatAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalGrossAmount = this.totalNetAmount.add(this.totalVatAmount);
    }
}