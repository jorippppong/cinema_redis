package com.cinema.rds.domains.ticketing;

import java.time.LocalDateTime;

import com.cinema.rds.domains.common.BaseEntity;
import com.cinema.rds.domains.schedule.ScheduleEntity;
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

@Entity
@Table(name = "ticket")
public class TicketEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime reservedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ScheduleEntity schedule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private UserEntity user;

	protected TicketEntity() {
	}

	public TicketEntity(LocalDateTime reservedAt, ScheduleEntity schedule, UserEntity user) {
		this.reservedAt = reservedAt;
		this.schedule = schedule;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getReservedAt() {
		return reservedAt;
	}

	public ScheduleEntity getSchedule() {
		return schedule;
	}

	public UserEntity getUser() {
		return user;
	}
}
