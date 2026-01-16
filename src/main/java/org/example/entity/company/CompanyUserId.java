package org.example.entity.company;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
@Embeddable
public class CompanyUserId implements Serializable {
    private UUID userId;
    private UUID companyId;
}