package io.tickets.ticketingBackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This auto-increments primary key
    private Long id;

    @Column(name = "ticket_number", nullable = false)
    private int ticketNum;

    @Column(name = "is_sold")
    private boolean isSold;

    @Column(name = "vendorId")
    private int vendorId;

    @Column(name = "customerId")
    private int customerId;

    public Ticket() {
    }

    public Ticket(int ticketNum) {
        this.ticketNum = ticketNum;
        this.isSold = false;
    }

    // Getters and setters
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public int getTicketCode() {
        return ticketNum;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketCode='" + ticketNum + '\'' +
                ", isSold=" + isSold +
                ", vendorId=" + vendorId +
                ", customerId=" + customerId +
                '}';
    }
}

