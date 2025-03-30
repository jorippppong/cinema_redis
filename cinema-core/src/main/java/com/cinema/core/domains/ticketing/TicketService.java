package com.cinema.core.domains.ticketing;

import static com.cinema.core.support.CoreErrorCode.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cinema.core.domains.schedule.ScheduleService;
import com.cinema.core.domains.screen.SeatRepository;
import com.cinema.core.domains.user.UserService;
import com.cinema.core.support.CoreErrorCode;
import com.cinema.core.support.CoreException;

@Service
public class TicketService {
	private static final int MAX_SEATS = 5;
	private final UserService userService;
	private final ScheduleService scheduleService;
	private final TicketRepository ticketRepository;
	private final SeatRepository seatRepository;

	public TicketService(UserService userService, ScheduleService scheduleService, TicketRepository ticketRepository,
		SeatRepository seatRepository) {
		this.userService = userService;
		this.scheduleService = scheduleService;
		this.ticketRepository = ticketRepository;
		this.seatRepository = seatRepository;
	}

	public void createTicketing(CreateTicketingCommand command) {
		// 존재하는 유저인지
		userService.findById(command.userId());

		// 존재하는 스케줄인지
		scheduleService.findById(command.scheduleId());

		// 연속한 좌석인지 확인
		validateSequenceSeat(command.seats());

		// 상영관에 존재하는 좌석인지
		validateSeatExist(command.seats());

		// 요청한 자리 + 이미 예약한 자리 = 5개 넘는지 체크
		int existTicketSeat = ticketRepository.findByScheduleIdAndUserId(command.scheduleId(), command.userId())
			.map(ticket -> ticket.seats()
				.size())
			.orElse(0);
		validateMaxSeatSize(existTicketSeat + command.seats()
			.size());

		// 예약 가능한 좌석인지
		validateSeatBookable(command.scheduleId(), command.seats());

		// ticketing 생성
		Set<Seat> seats = getByScreenIdAndSeatNumber(command.screenId(), command.seats());
		ticketRepository.save(command.userId(), command.scheduleId(), seats);
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

	private void validateSeatBookable(Long scheduleId, Set<String> seats) {
		Set<String> bookedSeats = ticketRepository.findBookedSeatsByScheduledId(scheduleId);
		for (String seat : seats) {
			if (bookedSeats.contains(seat)) {
				throw new CoreException(SEAT_ALREADY_BOOKED);
			}
		}
	}

	private Set<Seat> getByScreenIdAndSeatNumber(Long screenId, Set<String> seatNumbers) {
		return seatRepository.getByScreenIdAndSeatNumbers(screenId, seatNumbers);
	}
}
