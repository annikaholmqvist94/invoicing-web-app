package org.example.entity.user;

import org.example.entity.company.CompanyDTO;

import java.util.List;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        List<CompanyDTO> companies // Denna är nyckeln!
) {
    public static UserDTO fromEntity(User user) {
        if (user == null) return null;

        List<CompanyDTO> companyList = user.getCompanies() != null
                ? user.getCompanies().stream()
                .map(CompanyDTO::fromEntity)
                .toList()
                : List.of();

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                companyList
        );
    }
}
