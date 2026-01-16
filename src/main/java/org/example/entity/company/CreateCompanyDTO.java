package org.example.entity.company;

public record CreateCompanyDTO(
        String name,
        String orgNum,
        String email
) {}