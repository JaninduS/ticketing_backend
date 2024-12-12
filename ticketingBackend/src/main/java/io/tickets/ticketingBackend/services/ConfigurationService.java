package io.tickets.ticketingBackend.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.tickets.ticketingBackend.model.Configuration;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

@Service
public class ConfigurationService {
    private static final String CONFIG_FILE_PATH = "C:/Users/janin/OneDrive/Desktop/system_configuration.json";
    private final Gson gson = new Gson();

    // Load configuration from the file
    public Configuration loadConfiguration() throws IOException {
        File configFile = new File(CONFIG_FILE_PATH);
        if (!configFile.exists()) {
            throw new IOException("Configuration file not found at " + CONFIG_FILE_PATH);
        }
        try (Reader reader = new FileReader(configFile)) {
            return gson.fromJson(reader, Configuration.class);
        }
    }

    public void saveConfiguration(Configuration updatedConfig) throws IOException {
        File configFile = new File(CONFIG_FILE_PATH);
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(updatedConfig, writer);
        }
    }

}
