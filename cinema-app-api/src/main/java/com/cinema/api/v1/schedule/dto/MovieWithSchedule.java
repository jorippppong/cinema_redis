package com.cinema.api.v1.schedule.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cinema.core.domains.schedule.Schedule;

public record MovieWithSchedule(
	Long movieId,
	String title,
	String Rating,
	LocalDateTime releasedAt,
	String posterUrl,
	Integer runningTime,
	String genre,
	List<ScreenDetail> screens
) {
	public static List<MovieWithSchedule> from(List<Schedule> schedules) {
		return schedules.stream()
			.collect(Collectors.groupingBy(Schedule::movieId))
			.entrySet()
			.stream()
			.map(entry -> {
				Long movieId = entry.getKey();
				List<Schedule> groupByMovie = entry.getValue();
				Schedule movieInfo = groupByMovie.get(0);
				List<ScreenDetail> screenDetails = groupByMovie.stream()
					.collect(Collectors.groupingBy(Schedule::screenId))
					.entrySet()
					.stream()
					.map(screenEntry -> {
						Long screenId = screenEntry.getKey();
						String screenName = screenEntry.getValue()
							.get(0)
							.screenName();
						List<ScheduleDetail> scheduleDetails = screenEntry.getValue()
							.stream()
							.sorted(Comparator.comparing(Schedule::startTime))
							.map(dto -> new ScheduleDetail(dto.startTime(), dto.endTime()))
							.toList();
						return new ScreenDetail(screenId, screenName, scheduleDetails);
					})
					.toList();

				return new MovieWithSchedule(
					movieId,
					movieInfo.title(),
					movieInfo.rating()
						.toString(),
					movieInfo.releasedAt(),
					movieInfo.posterUrl(),
					movieInfo.runningTime(),
					movieInfo.genre()
						.toString(),
					screenDetails
				);
			})
			.sorted(Comparator.comparing(MovieWithSchedule::releasedAt)
				.reversed())
			.toList();
	}

}
