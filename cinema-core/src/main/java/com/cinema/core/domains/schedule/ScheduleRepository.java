package com.cinema.core.domains.schedule;

import com.cinema.core.domains.movie.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    List<MovieScheduleProjection> getSchedule(String title, Genre genre);

    Optional<MovieScheduleProjection> findById(Long id);
}
