package com.cinema.core.domains.ticketing;

import com.cinema.core.domains.common.BaseEntity;
import com.cinema.core.domains.schedule.ScheduleEntity;
import com.cinema.core.domains.user.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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
