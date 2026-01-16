package org.example.service;

import org.example.entity.client.Client;
import org.example.repository.ClientRepository;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    public ClientService(ClientRepository clientRepository, CompanyRepository companyRepository) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
    }

    public List<Client> getClientsByCompany(UUID companyId) {
        return clientRepository.findByCompanyId(companyId);
    }

    @Transactional
    public Client createClient(Client client) {
        // Här behövs ingen manuell transaktionshantering längre!
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }
}