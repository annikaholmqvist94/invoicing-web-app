package org.example.entity.company;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.entity.client.Client;
import org.example.entity.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "org_num", nullable = false)
    private String orgNum;

    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;

    @OneToMany(mappedBy = "company")
    private List<Client> clients;


    @ManyToMany(mappedBy = "companies")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}