package io.tickets.ticketingBackend.repository;

import io.tickets.ticketingBackend.model.LogMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogMessageRepository extends JpaRepository<LogMessage, Long> {
}
