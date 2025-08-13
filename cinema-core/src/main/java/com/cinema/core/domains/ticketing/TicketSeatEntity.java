package com.cinema.core.domains.ticketing;

import com.cinema.core.domains.common.BaseEntity;
import com.cinema.core.domains.screen.SeatEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "ticket_seat")
public class TicketSeatEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private TicketEntity ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SeatEntity seat;

    protected TicketSeatEntity() {
    }

    public TicketSeatEntity(TicketEntity ticket, SeatEntity seat) {
        this.ticket = ticket;
        this.seat = seat;
    }

    public Long getId() {
        return id;
    }

    public TicketEntity getTicket() {
        return ticket;
    }

    public SeatEntity getSeat() {
        return seat;
    }
}
