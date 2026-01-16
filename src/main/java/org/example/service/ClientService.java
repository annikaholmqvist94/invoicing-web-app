package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.client.Client;
import org.example.entity.client.ClientDTO;
import org.example.entity.company.Company;
import org.example.exception.EntityNotFoundException;
import org.example.repository.ClientRepository;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    public List<ClientDTO> getClientsByCompany(UUID companyId) {
        return clientRepository.findByCompanyId(companyId).stream()
                .map(ClientDTO::fromEntity)
                .toList();
    }

    @Transactional
    public ClientDTO createClient(CreateClientDTO dto) {
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));

        Client client = Client.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .company(company)
                .build();

        return ClientDTO.fromEntity(clientRepository.save(client));
    }

    @Transactional
    public void deleteClient(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Klienten hittades inte");
        }
        clientRepository.deleteById(clientId);
    }
}