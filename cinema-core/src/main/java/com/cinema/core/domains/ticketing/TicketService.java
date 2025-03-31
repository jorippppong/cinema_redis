package com.cinema.core.domains.ticketing;

import com.cinema.core.domains.schedule.Schedule;
import com.cinema.core.domains.schedule.ScheduleService;
import com.cinema.core.domains.screen.SeatRepository;
import com.cinema.core.domains.user.UserService;
import com.cinema.core.support.exception.CoreErrorCode;
import com.cinema.core.support.exception.CoreException;
import com.cinema.core.support.lock.DistributedLock;
import com.cinema.core.support.lock.FunctionalDistributedLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.cinema.core.support.exception.CoreErrorCode.*;

@Service
public class TicketService {
    private static final int MAX_SEATS = 5;
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final FunctionalDistributedLock functionalDistributedLock;

    public TicketService(UserService userService, ScheduleService scheduleService, TicketRepository ticketRepository,
                         SeatRepository seatRepository, ReservationRepository reservationRepository, FunctionalDistributedLock functionalDistributedLock) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
        this.functionalDistributedLock = functionalDistributedLock;
    }

    @Transactional
    public void createTicketing(CreateTicketingCommand command) {

        // 존재하는 유저인지
        userService.findById(command.userId());

        // 존재하는 스케줄인지
        Schedule schedule = scheduleService.findById(command.scheduleId());

        // 연속한 좌석인지 확인
        validateSequenceSeat(command.seats());

        // 상영관에 존재하는 좌석인지
        validateSeatExist(command.seats());

        // 요청한 자리 + 이미 예약한 자리 = 5개 넘는지 체크
        int existReservation = reservationRepository.countByScheduleIdAndUserId(command.scheduleId(),
                command.userId());
        validateMaxSeatSize(existReservation + command.seats()
                .size());

        // 예약 가능한 좌석인지
        Set<Seat> seats = getByScreenIdAndSeatNumber(schedule.screenId(), command.seats());
        List<Long> seatIds = seats.stream()
                .map(Seat::seatId)
                .toList();
        String lockName = "lock:schedule:" + command.scheduleId().toString();
        //validateSeatBookable(command.scheduleId(), seatIds, lockName);

        // 예약 및 ticket 생성
        //reservationRepository.reserve(command.userId(), command.scheduleId(), seatIds);
        //ticketRepository.save(command.userId(), command.scheduleId(), seats);

        // FIXME : 함수형 분산락 사용
        try {
            functionalDistributedLock.withLock(lockName, 10, 30, TimeUnit.SECONDS, () -> {
                validateSeatBookable(command.scheduleId(), seatIds);
                reservationRepository.reserve(command.userId(), command.scheduleId(), seatIds);
                //ticketRepository.save(command.userId(), command.scheduleId(), seats);
                return null;
            });
        } catch (Throwable throwable) {
            throw new CoreException(SEAT_ALREADY_BOOKED);
        }

    }

    private void validateMaxSeatSize(int totalSeat) {
        if (totalSeat > MAX_SEATS) {
            throw new CoreException(CoreErrorCode.OVER_MAX_SEAT_SIZE);
        }
    }

    private void validateSeatExist(Set<String> seats) {
        // 해당 서비스에서는 모든 상영관의 좌석이 행(A~E), 열(1~5)로 통일
        for (String seat : seats) {
            if (!seat.matches("^[A-E][1-5]$")) {
                throw new CoreException(SEAT_NOT_FOUND);
            }
        }
    }

    private void validateSequenceSeat(Set<String> seats) {
        Set<String> rows = seats.stream()
                .map(seat -> seat.substring(0, 1))
                .collect(Collectors.toSet());
        if (rows.size() > 1) {
            throw new CoreException(SEAT_NOT_IN_SEQUENCE);
        }

        List<Integer> columns = seats.stream()
                .map(seat -> Integer.parseInt(seat.substring(1)))
                .sorted()
                .toList();
        for (int i = 0; i < columns.size() - 1; i++) {
            if (columns.get(i) + 1 != columns.get(i + 1)) {
                throw new CoreException(SEAT_NOT_IN_SEQUENCE);
            }
        }
    }

    @DistributedLock(key = "#lockName")
    private void validateSeatBookable(Long scheduleId, List<Long> seatIds, String lockName) {
        List<Reservation> reservations = reservationRepository.getByScheduleIdAndSeatIds(scheduleId,
                seatIds);
        for (Reservation reservation : reservations) {
            if (reservation.isReserved()) {
                throw new CoreException(SEAT_ALREADY_BOOKED);
            }
        }
    }

    private void validateSeatBookable(Long scheduleId, List<Long> seatIds) {
        List<Reservation> reservations = reservationRepository.getByScheduleIdAndSeatIds(scheduleId,
                seatIds);
        for (Reservation reservation : reservations) {
            if (reservation.isReserved()) {
                throw new CoreException(SEAT_ALREADY_BOOKED);
            }
        }
    }

    private Set<Seat> getByScreenIdAndSeatNumber(Long screenId, Set<String> seatNumbers) {
        return seatRepository.getByScreenIdAndSeatNumbers(screenId, seatNumbers);
    }

}
