package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.client.Client;
import org.example.entity.client.ClientDTO;
import org.example.entity.client.CreateClientDTO;
import org.example.entity.client.UpdateClientDTO;
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
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<ClientDTO> getClientsByCompany(UUID companyId) {
        log.debug("Hämtar klienter för företag: {}", companyId);
        return clientRepository.findByCompanyId(companyId).stream()
                .map(ClientDTO::fromEntity)
                .toList();
    }

    @Transactional
    public ClientDTO createClient(CreateClientDTO dto) {
        log.info("Skapar ny klient med e-post: {}", dto.email());

        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Företaget hittades inte"));

        // Vi använder Builder-mönstret från din Client-entitet
        Client client = Client.builder()
                .company(company)
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .address(dto.address())
                .city(dto.city())
                .country(dto.country())
                .phoneNumber(dto.phoneNumber())
                .build();

        Client savedClient = clientRepository.save(client);
        return ClientDTO.fromEntity(savedClient);
    }

    @Transactional
    public ClientDTO updateClient(UpdateClientDTO dto) {
        log.info("Uppdaterar klient med ID: {}", dto.clientId());

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Klienten hittades inte"));

        // Uppdatera endast de fält som skickats med (inte null)
        if (dto.firstName() != null) client.setFirstName(dto.firstName());
        if (dto.lastName() != null) client.setLastName(dto.lastName());
        if (dto.email() != null) client.setEmail(dto.email());
        if (dto.address() != null) client.setAddress(dto.address());
        if (dto.city() != null) client.setCity(dto.city());
        if (dto.country() != null) client.setCountry(dto.country());
        if (dto.phoneNumber() != null) client.setPhoneNumber(dto.phoneNumber());

        return ClientDTO.fromEntity(clientRepository.save(client));
    }

    @Transactional
    public void deleteClient(UUID clientId) {
        log.warn("Tar bort klient: {}", clientId);
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Klienten hittades inte och kunde inte raderas");
        }
        clientRepository.deleteById(clientId);
    }
}