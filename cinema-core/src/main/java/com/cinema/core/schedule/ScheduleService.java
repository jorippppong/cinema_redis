package com.cinema.core.schedule;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.core.movie.Genre;

@Service
public class ScheduleService {
	private final ScheduleReader scheduleReader;

	public ScheduleService(ScheduleReader scheduleReader) {
		this.scheduleReader = scheduleReader;
	}

	// 상영중인 영화 조회
	public List<Schedule> getOngoingSchedule(String title, Genre genre) {
		return scheduleReader.getSchedule(title, genre);
	}
}
