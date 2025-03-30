package com.cinema.core.domains.schedule;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cinema.core.domains.movie.Genre;

@Repository
public interface ScheduleRepository {
	List<Schedule> getSchedule(String title, Genre genre);

	Optional<Schedule> findById(Long id);
}
