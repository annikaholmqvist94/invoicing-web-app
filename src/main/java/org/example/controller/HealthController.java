package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.time.LocalDateTime;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> getStatus() {
        return Map.of(
                "status", "UP",
                "application", "Invoicing Web App Backend",
                "environment", "Azure Cloud",
                "timestamp", LocalDateTime.now()
        );
    }
}