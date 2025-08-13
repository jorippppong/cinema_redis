package com.cinema.core.domains.screen;

import com.cinema.core.domains.common.BaseEntity;
import com.cinema.core.domains.ticketing.Seat;
import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class SeatEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScreenEntity screen;

    private String seatNumber;

    public Long getId() {
        return id;
    }

    public ScreenEntity getScreen() {
        return screen;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public Seat toSeat() {
        return new Seat(id, seatNumber);
    }
}
