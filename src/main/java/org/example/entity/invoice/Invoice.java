package org.example.entity.invoice;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {
    // ... tidigare fält (id, company, client, etc)

    private BigDecimal totalNetAmount; // Summan av alla rader exkl. moms
    private BigDecimal totalVatAmount; // Summan av all moms
    private BigDecimal totalGrossAmount; // Att betala (Netto + Moms)

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceItem> items;

    // Metod för att uppdatera summorna baserat på listan av items
    public void recalculateTotals() {
        this.totalNetAmount = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalVatAmount = items.stream()
                .map(item -> item.getVatAmount() != null ? item.getVatAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalGrossAmount = this.totalNetAmount.add(this.totalVatAmount);
    }
}