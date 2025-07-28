package com.cinema.rds.domains.ticketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSeatJpaRepository extends JpaRepository<TicketSeatEntity, Long> {
	List<TicketSeatEntity> findAllByTicketId(Long ticketId);
}
