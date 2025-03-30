package com.cinema.rds.domains.ticketing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cinema.core.domains.ticketing.Reservation;

import jakarta.persistence.LockModeType;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
	int countByScheduleIdAndUserId(Long scheduleId, Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select r from ReservationEntity r where r.schedule.id = :scheduleId and r.seat.id in :seatIds")
	List<Reservation> getByScheduleIdAndSeatId(@Param("scheduleId") Long scheduleId,
		@Param("seatIds") List<Long> seatIds);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update ReservationEntity r set r.user.id = :userId, r.isReserved = true " +
		"where r.schedule.id = :scheduleId and r.seat.id in :seatIds")
	void updateReservation(@Param("userId") Long userId,
		@Param("scheduleId") Long scheduleId,
		@Param("seatIds") List<Long> seatIds);
}
