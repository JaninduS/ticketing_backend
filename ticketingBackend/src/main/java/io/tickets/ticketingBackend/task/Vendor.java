package io.tickets.ticketingBackend.task;

import io.tickets.ticketingBackend.model.Ticket;
import io.tickets.ticketingBackend.services.TicketPoolService;

import java.util.UUID;

public class Vendor implements Runnable {
    private final int vendorId;
    private final TicketPoolService ticketPoolService;
    private final int ticketReleaseRate;
    private final int maxTicketCapacity;


    public Vendor(int vendorId, TicketPoolService ticketPoolService, int ticketReleaseRate) {
        this.vendorId = vendorId;
        this.ticketPoolService = ticketPoolService;
        this.ticketReleaseRate = ticketReleaseRate;
        this.maxTicketCapacity = ticketPoolService.getMaxTicketCapacity();
    }

    public void logMessage(String logMessage) {
        System.out.println(logMessage);
        ticketPoolService.logMessage(logMessage);
    }

    @Override
    public void run() {
        try {
            while (ticketPoolService.isRunning() && ticketPoolService.canProduceMoreTickets()) {
                synchronized (ticketPoolService.getTicketPool()) { // Synchronize access to the ticket pool
                    if (Thread.interrupted()) return;
                    // Wait if the ticket pool is at max capacity
                    while (ticketPoolService.getTicketPool().size() >= maxTicketCapacity) {
                        logMessage("Vendor " + vendorId + " is waiting, ticket pool is full...");
                        ticketPoolService.getTicketPool().wait(); // Wait until space is available
                    }

                    // Add a ticket if there's room
                    if (ticketPoolService.canProduceMoreTickets()) {
                        Ticket ticket = new Ticket((ticketPoolService.getTicketsProduced() + 1)); // Create a new ticket
                        ticketPoolService.addTicket(ticket, vendorId); // Add the ticket to the pool
                        ticketPoolService.getTicketPool().notifyAll();
                    }
                }
                // Sleep for the ticket release rate
                Thread.sleep(ticketReleaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

