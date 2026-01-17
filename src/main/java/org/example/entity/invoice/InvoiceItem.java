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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    private String name;
    private int quantity;
    private BigDecimal unitPrice;

    // Detta fält tillåter nu manuell inmatning från användaren
    private BigDecimal vatAmount;

    public BigDecimal getLineNetAmount() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Denna används av Invoice.recalculateTotals()
    public BigDecimal getTotalLineAmount() {
        BigDecimal net = getLineNetAmount();
        BigDecimal vat = (vatAmount != null) ? vatAmount : BigDecimal.ZERO;
        return net.add(vat);
    }
}