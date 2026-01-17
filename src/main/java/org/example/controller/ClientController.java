package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.entity.client.ClientDTO;
import org.example.entity.client.CreateClientDTO;
import org.example.entity.client.UpdateClientDTO;
import org.example.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody CreateClientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(dto));
    }


    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ClientDTO>> getClientsByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(clientService.getClientsByCompany(companyId));
    }


    @PutMapping
    public ResponseEntity<ClientDTO> updateClient(@Valid @RequestBody UpdateClientDTO dto) {
        return ResponseEntity.ok(clientService.updateClient(dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}