package com.cinema.core.domains.ticketing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketSeatJpaRepository extends JpaRepository<TicketSeatEntity, Long> {
    List<TicketSeatEntity> findAllByTicketId(Long ticketId);
}
