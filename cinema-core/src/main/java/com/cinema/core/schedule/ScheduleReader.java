package com.cinema.core.schedule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cinema.core.movie.Genre;

@Component
public class ScheduleReader {
	private final ScheduleRepository scheduleRepository;

	public ScheduleReader(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	public List<Schedule> getSchedule(String title, Genre genre) {
		return scheduleRepository.getSchedule(title, genre);
	}
}
