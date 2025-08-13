package com.cinema.core.domains.ticketing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findByScheduleIdAndUserId(Long scheduleId, Long userId);

    @Query("select ts.seat.seatNumber from TicketSeatEntity ts where ts.ticket.schedule.id = :scheduleId")
    Set<String> findBookedSeatsByScheduleId(@Param("scheduleId") Long scheduleId);
}
