package io.tickets.ticketingBackend.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


import io.tickets.ticketingBackend.model.LogMessage;
import io.tickets.ticketingBackend.model.Ticket;
import io.tickets.ticketingBackend.repository.LogMessageRepository;
import io.tickets.ticketingBackend.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketPoolService {
    private final List<Ticket> ticketPool;
    private final TicketRepository ticketRepository;
    private int maxTicketCapacity;
    private static boolean running =  false;
    private int ticketsProduced = 0;
    private int ticketsProcessed = 0;
    private int totalTickets;
    private final LogMessageRepository logMessageRepository;

    public TicketPoolService(TicketRepository ticketRepository, LogMessageRepository logMessageRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketPool = Collections.synchronizedList(new ArrayList<>());
        this.logMessageRepository = logMessageRepository;
    }

    public void logMessage(String message) {
        LogMessage log = new LogMessage(message);
        logMessageRepository.save(log);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean state) {
        running = state;
    }

    // Synchronized getter and incrementer for ticketsProduced
    public synchronized  int getTicketsProduced() {
        return ticketsProduced;
    }

    public synchronized  void incrementTicketsProduced() {
        ticketsProduced++;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    // Stop condition for vendors
    public synchronized boolean canProduceMoreTickets() {
        return ticketsProduced < totalTickets;
    }

    // Stop condition for customers
    public synchronized boolean canProcessMoreTickets() {
        return ticketsProcessed < totalTickets;
    }

    public synchronized List<Ticket> getTicketPool() {
        return ticketPool;
    }

    public int getSize() {
        return ticketPool.size();
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public synchronized void incrementTicketsProcessed() {
        ticketsProcessed++;
    }

    // Initialize ticket pool with user-defined capacity
    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.maxTicketCapacity = maxCapacity;
        System.out.println("Ticket pool initialized with max capacity: " + maxCapacity);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<LogMessage> getAllLogs() {
        return logMessageRepository.findAll();
    }

    // Clear database and ticketpool
    public void configure() {
        ticketRepository.deleteAll(); // Deletes all previous tickets
        logMessageRepository.deleteAll(); // Delete all previous logs
        ticketPool.clear(); // Clears ticketPool
        ticketsProcessed = 0;
        ticketsProduced = 0;
    }

    public synchronized void addTicket(Ticket ticket, int vendorId) {
        try {
            ticket.setVendorId(vendorId);
            ticketPool.add(ticket);
            incrementTicketsProduced();
            String logMessage = "Vendor " + vendorId + " added ticket: " + ticket + "Current pool size: " + getSize();
            System.out.println(logMessage);
            logMessage(logMessage);
        } catch (Exception e) {
            String logMessage = "Error adding ticket: " + e.getMessage();
            System.out.println(logMessage);
            logMessage(logMessage);
        }
    }

    public synchronized void buyTicket(int customerId) {
        try {
            Ticket ticket = ticketPool.remove(0);
            ticket.setSold(true);
            ticket.setCustomerId(customerId);
            incrementTicketsProcessed();

            ticketRepository.save(ticket);
            String logMessage = "Customer " + customerId + " purchased ticket: " + ticket;
            System.out.println(logMessage);
            logMessage(logMessage);

        } catch (Exception e) {
            String logMessage = "Customer " + customerId + " encountered an error while buying" + e.getMessage();
            System.out.println(logMessage);
            logMessage(logMessage);
        }
    }


}

