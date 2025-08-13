package com.cinema.core.domains.ticketing;

import com.cinema.core.domains.common.BaseEntity;
import com.cinema.core.domains.schedule.ScheduleEntity;
import com.cinema.core.domains.screen.SeatEntity;
import com.cinema.core.domains.user.UserEntity;
import jakarta.persistence.*;

/**
 * 상영 시간표가 배정되면 해당 상영관에 있는 자리(총 25개)가 미리 등록됨
 */
@Entity
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScheduleEntity schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SeatEntity seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    private Boolean isReserved;

//     FIXME : 낙관적 Lock
//	@Version
//	private int version;

    protected ReservationEntity() {

    }

    public Reservation toReservation() {
        return new Reservation(id, schedule.getId(), seat.getId(), isReserved);
    }

//    public int getVersion() {
//        return version;
//    }
}
