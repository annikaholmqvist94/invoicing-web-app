package org.example.entity.company;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.client.Client;
import org.example.entity.invoice.Invoice;
import org.example.entity.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"clients", "invoices", "users"})
public class Company {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "org_num", nullable = false, unique = true, length = 50)
    private String orgNum;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String name;

    private String address;
    private String city;
    private String country;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationer
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Invoice> invoices = new HashSet<>();

    /**
     * Om du väljer att behålla CompanyUser-klassen för rollstyrning.
     * Om du väljer @ManyToMany-lösningen byts denna ut mot Set<User> users.
     */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CompanyUser> users = new HashSet<>();

    // Hjälpmetoder för att hålla logiken ren i din Service

    public void updateFromDTO(UpdateCompanyDTO dto) {
        if (dto.name() != null) this.name = dto.name();
        if (dto.orgNum() != null) this.orgNum = dto.orgNum();
        if (dto.email() != null) this.email = dto.email();
        if (dto.address() != null) this.address = dto.address();
        if (dto.city() != null) this.city = dto.city();
        if (dto.country() != null) this.country = dto.country();
        if (dto.phoneNumber() != null) this.phoneNumber = dto.phoneNumber();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Company company = (Company) o;
        return getId() != null && Objects.equals(getId(), company.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}