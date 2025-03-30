package com.cinema.core.domains.ticketing;

import java.util.Set;

public record CreateTicketingCommand(
	Long userId,
	Long scheduleId,
	Long screenId,
	Set<String> seats
) {
}
