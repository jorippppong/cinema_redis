package com.cinema.core.domains.ticketing;

import java.util.Set;

public record CreateTicketingCommand(
	Long userId,
	Long scheduleId,
	Set<String> seats
) {
}
