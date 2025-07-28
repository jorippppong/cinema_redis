package com.cinema.core.domains.ticketing;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {
	int countByScheduleIdAndUserId(Long scheduleId, Long userId);

	void reserve(Long userId, Long scheduleId, List<Long> seatIds);

	List<Reservation> getByScheduleIdAndSeatIds(Long scheduleId, List<Long> seatIds);
}
