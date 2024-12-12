package io.tickets.ticketingBackend.controller;

import io.tickets.ticketingBackend.model.Ticket;
import io.tickets.ticketingBackend.services.SimulationService;
import io.tickets.ticketingBackend.services.TicketPoolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tickets")
public class TicketingController {

    private final SimulationService simulationService;
    private final TicketPoolService ticketPoolService;

    public TicketingController(SimulationService simulationService, TicketPoolService ticketPoolService) {
        this.simulationService = simulationService;
        this.ticketPoolService = ticketPoolService;
    }

    // Endpoint to get all tickets currently in the pool
    @GetMapping("/get-all")
    public List<Ticket> getAllTickets() {
        return ticketPoolService.getAllTickets();
    }

}

