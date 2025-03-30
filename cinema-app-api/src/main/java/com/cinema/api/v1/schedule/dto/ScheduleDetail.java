package com.cinema.api.v1.schedule.dto;

import java.time.LocalDateTime;

public record ScheduleDetail(
	LocalDateTime startAt,
	LocalDateTime endAt
) {
}
