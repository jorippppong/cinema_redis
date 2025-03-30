package com.cinema.core.domains.ticketing;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository {
	Optional<Ticket> findByScheduleIdAndUserId(Long scheduleId, Long userId);

	void save(Long userId, Long scheduleId, Set<Seat> seats);

	Set<String> findBookedSeatsByScheduledId(Long scheduleId);
}
