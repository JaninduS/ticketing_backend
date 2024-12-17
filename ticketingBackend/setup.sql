-- Create the database
CREATE DATABASE IF NOT EXISTS ticketing_system;

-- Use the database
USE ticketing_system;

-- Create the Ticket table
CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_code VARCHAR(255) NOT NULL,
    is_sold BOOLEAN DEFAULT FALSE,
    vendor_id INT DEFAULT NULL,
    customer_id INT DEFAULT NULL
);

-- Insert sample data
INSERT INTO ticket (ticket_code, is_sold, vendor_id, customer_id) VALUES
('TICKET-001', FALSE, 1, NULL),
('TICKET-002', FALSE, 2, NULL);
