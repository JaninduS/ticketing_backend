package io.tickets.ticketingBackend.controller;

import io.tickets.ticketingBackend.model.Configuration;
import io.tickets.ticketingBackend.services.SimulationService;
import io.tickets.ticketingBackend.services.TicketPoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/simulation")
public class SimulationController {
    private final SimulationService simulationService;
    private final TicketPoolService ticketPoolService;

    public SimulationController(SimulationService simulationService, TicketPoolService ticketPoolService) {
        this.simulationService = simulationService;
        this.ticketPoolService = ticketPoolService;
    }

    // Endpoint to start the simulation
    @PostMapping("/start")
    public String startSimulation(@RequestBody Configuration configuration) {
        int totalTickets = configuration.getTotalTickets();
        int maxTicketCapacity = configuration.getMaxTicketCapacity();
        int ticketReleaseRate = configuration.getTicketReleaseRate();
        int customerRetrievalRate = configuration.getCustomerRetrievalRate();
        int vendorCount = configuration.getVendorCount();
        int customerCount = configuration.getCustomerCount();

        ticketPoolService.setTotalTickets(totalTickets);
        ticketPoolService.setMaxCapacity(maxTicketCapacity);
        ticketPoolService.setTotalTickets(totalTickets);

        simulationService.startSimulation(vendorCount, customerCount, ticketReleaseRate, customerRetrievalRate);
        return "Simulation started with " + vendorCount + " vendors and " + customerCount + " customers.";
    }
    // Endpoint to stop the simulation
    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        simulationService.stopSimulation();
        return ResponseEntity.ok("Simulation stopped.");
    }
}
