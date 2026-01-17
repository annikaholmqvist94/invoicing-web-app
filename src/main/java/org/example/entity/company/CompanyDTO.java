package org.example.entity.company;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CompanyDTO(
        UUID id,
        String orgNum,
        String email,
        String phoneNumber,
        String name,
        String address,
        String city,
        String country,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Omvandlar en Company-entitet till en CompanyDTO.
     * Vi använder Builder-mönstret som genereras av Lombok.
     */
    public static CompanyDTO fromEntity(Company company) {
        if (company == null) {
            return null;
        }

        return CompanyDTO.builder()
                .id(company.getId())
                .orgNum(company.getOrgNum())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .name(company.getName())
                .address(company.getAddress())
                .city(company.getCity())
                .country(company.getCountry())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}