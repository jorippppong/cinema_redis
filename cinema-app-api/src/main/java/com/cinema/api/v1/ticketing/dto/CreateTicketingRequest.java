package com.cinema.api.v1.ticketing.dto;

import java.util.HashSet;
import java.util.List;

import com.cinema.core.domains.ticketing.CreateTicketingCommand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record CreateTicketingRequest(
	@Min(value = 1, message = "userId는 1 이상의 정수 입니다.")
	Long userId,
	@Min(value = 1, message = "scheduleId는 1 이상의 정수 입니다.")
	Long scheduleId,
	@NotEmpty(message = "예매하고 싶은 자리가 1개 이상 존재 해야 합니다.")
	List<String> seats
) {
	public CreateTicketingCommand toCreateTicketingCommand() {

		return new CreateTicketingCommand(userId, scheduleId, new HashSet<>(seats));
	}
}
