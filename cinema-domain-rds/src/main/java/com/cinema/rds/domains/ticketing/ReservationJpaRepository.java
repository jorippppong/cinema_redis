package com.cinema.rds.domains.ticketing;

import com.cinema.core.domains.ticketing.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
    int countByScheduleIdAndUserId(Long scheduleId, Long userId);

    // FIXME : 비관적 Lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ReservationEntity r where r.schedule.id = :scheduleId and r.seat.id in :seatIds")
    List<Reservation> getByScheduleIdAndSeatIdLock(@Param("scheduleId") Long scheduleId,
                                                   @Param("seatIds") List<Long> seatIds);

    @Query("select r from ReservationEntity r where r.schedule.id = :scheduleId and r.seat.id in :seatIds")
    List<Reservation> getByScheduleIdAndSeatId(@Param("scheduleId") Long scheduleId,
                                               @Param("seatIds") List<Long> seatIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ReservationEntity r set r.user.id = :userId, r.isReserved = true " +
            "where r.schedule.id = :scheduleId and r.seat.id in :seatIds")
    void updateReservation(@Param("userId") Long userId,
                           @Param("scheduleId") Long scheduleId,
                           @Param("seatIds") List<Long> seatIds);

    // FIXME : 낙관적 Lock
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query("update ReservationEntity r set r.user.id = :userId, r.isReserved = true, r.version = r.version + 1 " +
//            "where r.schedule.id = :scheduleId and r.seat.id in :seatIds")
//    void updateReservationWithVersion(@Param("userId") Long userId,
//                                      @Param("scheduleId") Long scheduleId,
//                                      @Param("seatIds") List<Long> seatIds);

    Optional<ReservationEntity> findByScheduleIdAndSeatId(Long scheduleId, Long seatId);
}
