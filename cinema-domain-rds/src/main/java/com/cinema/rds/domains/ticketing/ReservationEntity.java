package com.cinema.rds.domains.ticketing;

import com.cinema.core.domains.ticketing.Reservation;
import com.cinema.rds.domains.common.BaseEntity;
import com.cinema.rds.domains.schedule.ScheduleEntity;
import com.cinema.rds.domains.screen.SeatEntity;
import com.cinema.rds.domains.user.UserEntity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
	@JoinColumn(name = "user_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private UserEntity user;

	private Boolean isReserved;

	protected ReservationEntity() {

	}

	public Reservation toReservation() {
		return new Reservation(id, schedule.getId(), seat.getId(), isReserved);
	}

}
