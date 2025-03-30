package com.cinema.rds.domains.ticketing;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cinema.core.domains.ticketing.Reservation;
import com.cinema.core.domains.ticketing.ReservationRepository;

@Repository
public class ReservationCoreRepository implements ReservationRepository {
	private final ReservationJpaRepository reservationJpaRepository;

	public ReservationCoreRepository(ReservationJpaRepository reservationJpaRepository) {
		this.reservationJpaRepository = reservationJpaRepository;
	}

	@Override
	public int countByScheduleIdAndUserId(Long scheduleId, Long userId) {
		return reservationJpaRepository.countByScheduleIdAndUserId(scheduleId, userId);
	}

	@Override
	public void reserve(Long userId, Long scheduleId, List<Long> seatIds) {
		reservationJpaRepository.updateReservation(userId, scheduleId, seatIds);
	}

	@Override
	public List<Reservation> getByScheduleIdAndSeatIds(Long scheduleId, List<Long> seatIds) {
		//System.out.println("===========================================================5");
		return reservationJpaRepository.getByScheduleIdAndSeatId(scheduleId, seatIds);
	}
}
