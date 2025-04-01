package com.cinema.core.domains.ticketing;

import java.util.Set;

public record Ticket(
	Long ticketId,
	Long scheduleId,
	Set<TicketSeat> seats
) {
}
