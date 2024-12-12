package io.tickets.ticketingBackend.services;

import io.tickets.ticketingBackend.task.Customer;
import io.tickets.ticketingBackend.task.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {
    private final TicketPoolService ticketPoolService;

    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();


    public SimulationService(TicketPoolService ticketPoolService) {
        this.ticketPoolService = ticketPoolService;
    }

    public void startSimulation(int vendorCount, int customerCount, int ticketReleaseRate, int customerReleaseRate) {
        ticketPoolService.configure();
        ticketPoolService.setRunning(true);
        System.out.println("Simulation is running...");
        System.out.println("Can produce more tickets: " + ticketPoolService.canProduceMoreTickets());
        System.out.println("Can process more tickets: " + ticketPoolService.canProcessMoreTickets());
        // Create vendor threads
        for (int i = 0; i < vendorCount; i++) {
            Thread vendorThread = new Thread(new Vendor(i + 1, ticketPoolService, ticketReleaseRate));
            vendorThreads.add(vendorThread);
            vendorThread.start();
            System.out.println("Started Vendor thread " + i + 1);
        }

        // Create customer threads
        for (int i = 0; i < customerCount; i++) {
            Thread customerThread = new Thread(new Customer(i + 1, ticketPoolService, customerReleaseRate, this));
            customerThreads.add(customerThread);
            customerThread.start();
            System.out.println("Started Customer thread " + i + 1);
        }
    }

    public synchronized void stopSimulation() {
        if (!ticketPoolService.isRunning()) {
            String logMessage = "System is not running.";
            System.out.println(logMessage);
            ticketPoolService.logMessage(logMessage);
            return;
        }

        String logMessage = "Stopping ticket processing...";
        System.out.println(logMessage);
        ticketPoolService.logMessage(logMessage);

        ticketPoolService.setRunning(false); // Set the running flag to false
        // Stop vendor threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();
        }
        vendorThreads.clear();

        // Stop customer threads
        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();
        }
        customerThreads.clear();

        logMessage = "All threads stopped.";
        System.out.println(logMessage);
        ticketPoolService.logMessage(logMessage);
    }

    // Check if the simulation can be stopped
    public boolean canStopSimulation() {
        return !ticketPoolService.canProcessMoreTickets();  // Check if all tickets have been processed
    }
}
