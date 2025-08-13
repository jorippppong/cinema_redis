package com.cinema.core.domains.ticketing;

import com.cinema.core.domains.schedule.ScheduleEntity;
import com.cinema.core.domains.schedule.ScheduleJpaRepository;
import com.cinema.core.domains.screen.SeatEntity;
import com.cinema.core.domains.screen.SeatJpaRepository;
import com.cinema.core.domains.user.UserEntity;
import com.cinema.core.domains.user.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TicketCoreRepository implements TicketRepository {
    private final TicketJpaRepository ticketJpaRepository;
    private final TicketSeatJpaRepository ticketSeatJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final SeatJpaRepository seatJpaRepository;

    public TicketCoreRepository(TicketJpaRepository ticketJpaRepository,
                                TicketSeatJpaRepository ticketSeatJpaRepository, UserJpaRepository userJpaRepository,
                                ScheduleJpaRepository scheduleJpaRepository, SeatJpaRepository seatJpaRepository) {
        this.ticketJpaRepository = ticketJpaRepository;
        this.ticketSeatJpaRepository = ticketSeatJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.scheduleJpaRepository = scheduleJpaRepository;
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public Optional<Ticket> findByScheduleIdAndUserId(Long scheduleId, Long userId) {
        return ticketJpaRepository.findByScheduleIdAndUserId(scheduleId, userId)
                .map(ticket -> {
                    List<TicketSeatEntity> ticketSeats = ticketSeatJpaRepository.findAllByTicketId(ticket.getId());
                    return new Ticket(
                            ticket.getId(),
                            ticket.getSchedule()
                                    .getId(),
                            ticketSeats.stream()
                                    .map(s -> new TicketSeat(s.getSeat()
                                            .getSeatNumber()))
                                    .collect(Collectors.toSet())
                    );
                });
    }

    @Override
    public void save(Long userId, Long scheduleId, Set<Seat> seats) {
        UserEntity user = userJpaRepository.getReferenceById(userId);
        ScheduleEntity schedule = scheduleJpaRepository.getReferenceById(scheduleId);
        TicketEntity ticket = new TicketEntity(LocalDateTime.now(), schedule, user);
        ticketJpaRepository.save(ticket);
        Set<TicketSeatEntity> ticketSeats = seats.stream()
                .map(s -> {
                    SeatEntity seat = seatJpaRepository.getReferenceById(s.seatId());
                    return new TicketSeatEntity(ticket, seat);
                })
                .collect(Collectors.toSet());
        ticketSeatJpaRepository.saveAll(ticketSeats);
    }

    @Override
    public Set<String> findBookedSeatsByScheduledId(Long scheduleId) {
        return ticketJpaRepository.findBookedSeatsByScheduleId(scheduleId);
    }
}
