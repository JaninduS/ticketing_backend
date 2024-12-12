package io.tickets.ticketingBackend.task;

import io.tickets.ticketingBackend.services.SimulationService;
import io.tickets.ticketingBackend.services.TicketPoolService;

public class Customer implements Runnable {
    private final TicketPoolService ticketPoolService;
    private final int customerId;
    private final int customerRetrievalRate;
    private final SimulationService simulationService;

    public Customer(
            int customerId,
            TicketPoolService ticketPoolService,
            int customerRetrievalRate,
            SimulationService simulationService)
    {
        this.customerId = customerId;
        this.ticketPoolService = ticketPoolService;
        this.customerRetrievalRate = customerRetrievalRate;
        this.simulationService = simulationService;
    }

    public void logMessage(String logMessage) {
        System.out.println(logMessage);
        ticketPoolService.logMessage(logMessage);
    }

    @Override
    public void run() {
        String logMessage;
        try {
            while (ticketPoolService.isRunning() && ticketPoolService.canProcessMoreTickets()) {
                synchronized (ticketPoolService.getTicketPool()) {
                    if (Thread.interrupted()) return;
                    // Wait if the ticket pool is empty
                    while (ticketPoolService.getTicketPool().isEmpty()) {

                        logMessage("Customer " + customerId + " is waiting, ticket pool is empty...");

                        ticketPoolService.getTicketPool().wait();  // Wait until a ticket is available
                    }
                    if (ticketPoolService.canProcessMoreTickets()) {
                        ticketPoolService.buyTicket(customerId);  // Buy a ticket from the pool
                        ticketPoolService.getTicketPool().notifyAll();
                    }
                }

                // Sleep for the retrieval rate
                Thread.sleep(customerRetrievalRate);
            }
            if (!ticketPoolService.canProcessMoreTickets()) {
                simulationService.stopSimulation();
                logMessage("Application process completed!!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logMessage("Customer " + customerId + " was interrupted.");
        }
    }
}

