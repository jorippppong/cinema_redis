package com.cinema.rds.domains.ticketing;

import com.cinema.core.domains.ticketing.Reservation;
import com.cinema.core.domains.ticketing.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        // reservationJpaRepository.updateReservationWithVersion(userId, scheduleId, seatIds);  // FIXME : 낙관적 Lock
        reservationJpaRepository.updateReservation(userId, scheduleId, seatIds);
        ReservationEntity reservation = reservationJpaRepository.findByScheduleIdAndSeatId(scheduleId,
                        seatIds.get(0))
                .get();
//		System.out.println("[version] : " + reservation.getVersion());
    }

    @Override
    public List<Reservation> getByScheduleIdAndSeatIds(Long scheduleId, List<Long> seatIds) {
        return reservationJpaRepository.getByScheduleIdAndSeatId(scheduleId, seatIds);
    }
}
