package com.cinema.api.v1.schedule.dto;

import java.util.List;

public record ScreenDetail(
	Long screenId,
	String screenName,
	List<ScheduleDetail> schedules
) {
}
