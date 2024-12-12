package io.tickets.ticketingBackend.controller;

import io.tickets.ticketingBackend.model.Configuration;
import io.tickets.ticketingBackend.services.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/configuration")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfigurationController {

        private final ConfigurationService configurationService;

        public ConfigurationController(ConfigurationService configurationService) {
            this.configurationService = configurationService;
        }

        @GetMapping
        public ResponseEntity<Configuration> getConfiguration() {
            try {
                Configuration config = configurationService.loadConfiguration();
                return ResponseEntity.ok(config);
            } catch (IOException e) {
                return ResponseEntity.status(500).body(null);
            }
        }

        @PutMapping
        public ResponseEntity<Map<String, String>> updateConfiguration(@RequestBody Configuration updatedConfig) {
            Map<String, String> response = new HashMap<>();
            try {
                configurationService.saveConfiguration(updatedConfig);
                response.put("message", "Configuration updated successfully");
                return ResponseEntity.ok(response);
            } catch (IOException e) {
                response.put("error", "Error saving configuration: " + e.getMessage());
                return ResponseEntity.status(500).body(response);
            }
        }
    }

