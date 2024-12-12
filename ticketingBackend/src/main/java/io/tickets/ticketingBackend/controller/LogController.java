package io.tickets.ticketingBackend.controller;

import io.tickets.ticketingBackend.model.LogMessage;
import io.tickets.ticketingBackend.services.TicketPoolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/logs")
public class LogController {
    private final TicketPoolService ticketPoolService;

    public LogController(TicketPoolService ticketPoolService) {
        this.ticketPoolService = ticketPoolService;
    }

    @GetMapping("/all")
    public List<LogMessage> getAllLogs() {
        return ticketPoolService.getAllLogs();
    }
}
