package com.cinema.core.domains.ticketing;

public record Reservation(
	Long reservationId,
	Long scheduleId,
	Long seatId,
	Boolean isReserved
) {
}
