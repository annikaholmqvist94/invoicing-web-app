package org.example.entity.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CompanyUserId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "company_id")
    private UUID companyId;
}