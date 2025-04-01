package com.cinema.rds.domains.ticketing;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {
	Optional<TicketEntity> findByScheduleIdAndUserId(Long scheduleId, Long userId);

	@Query("select ts.seat.seatNumber from TicketSeatEntity ts where ts.ticket.schedule.id = :scheduleId")
	Set<String> findBookedSeatsByScheduleId(@Param("scheduleId") Long scheduleId);
}
