package org.example.entity.invoice;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    private Integer quantity;
    private BigDecimal unitPrice;

    // Ny parameter för moms
    private BigDecimal vatAmount;

    // Hjälpmetod för att beräkna radens total (Antal * Pris + Moms)
    public BigDecimal getTotalLineAmount() {
        BigDecimal base = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return base.add(vatAmount != null ? vatAmount : BigDecimal.ZERO);
    }

}