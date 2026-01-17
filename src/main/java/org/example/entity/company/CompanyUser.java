package org.example.entity.company;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.user.User;

@Entity
@Table(name = "company_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUser {

    @EmbeddedId
    private CompanyUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("companyId")
    @JoinColumn(name = "company_id")
    private Company company;


    public CompanyUser(User user, Company company) {
        this.user = user;
        this.company = company;
        this.id = new CompanyUserId(user.getId(), company.getId());
    }
}