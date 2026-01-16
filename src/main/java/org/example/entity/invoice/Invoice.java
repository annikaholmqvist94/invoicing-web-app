package org.example.entity.invoice;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.client.Client;
import org.example.entity.company.Company;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, unique = true)
    private String number;

    private String status; // t.ex. "DRAFT", "SENT", "PAID"

    private LocalDateTime dueDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Summery fields
    private BigDecimal totalNetAmount;   // Summa exkl. moms
    private BigDecimal totalVatAmount;   // Total moms
    private BigDecimal totalGrossAmount; // Att betala (Netto + Moms)

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    /**
     * Skapar en entitet från en CreateInvoiceDTO.
     * Denna metod använder hjälpmetoder för att säkerställa att summorna blir rätt direkt.
     */
    public static Invoice fromDTO(CreateInvoiceDTO dto, Company company, Client client) {
        Invoice invoice = Invoice.builder()
                .company(company)
                .client(client)
                .number(dto.number())
                .dueDate(dto.dueDate())
                .status("DRAFT")
                .items(new ArrayList<>())
                .build();

        if (dto.items() != null) {
            dto.items().forEach(itemDTO -> {
                InvoiceItem item = InvoiceItem.builder()
                        .name(itemDTO.name())
                        .quantity(itemDTO.quantity())
                        .unitPrice(itemDTO.unitPrice())
                        .invoice(invoice)
                        .build();
                invoice.items.add(item);
            });
        }

        invoice.recalculateTotals();
        return invoice;
    }

    /**
     * Lägger till en rad och kopplar den till denna faktura.
     */
    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
        recalculateTotals();
    }

    /**
     * Rensar rader, användbart vid uppdatering.
     */
    public void clearItems() {
        items.clear();
        recalculateTotals();
    }

    /**
     * Din önskade beräkningslogik som hanterar både netto, moms och brutto.
     */
    public void recalculateTotals() {
        this.totalNetAmount = items.stream()
                .map(InvoiceItem::getLineNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalVatAmount = items.stream()
                .map(item -> item.getVatAmount() != null ? item.getVatAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalGrossAmount = this.totalNetAmount.add(this.totalVatAmount);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Invoice invoice = (Invoice) o;
        return getId() != null && Objects.equals(getId(), invoice.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}