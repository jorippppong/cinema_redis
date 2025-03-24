package com.cinema.core.schedule;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cinema.core.movie.Genre;

@Repository
public interface ScheduleRepository {
	List<Schedule> getSchedule(String title, Genre genre);
}
