package org.example.entity.client;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.company.Company;

import java.util.UUID;

@Entity
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;
    private String address;
    private String city;
    private String country;
}
