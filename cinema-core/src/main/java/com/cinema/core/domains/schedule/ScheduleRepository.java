package com.cinema.core.domains.schedule;

import com.cinema.core.domains.excel.ScheduleRow;
import com.cinema.core.domains.movie.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ScheduleRepository {
    List<MovieScheduleProjection> getSchedule(String title, Genre genre);

    Optional<MovieScheduleProjection> findById(Long id);

    Stream<ScheduleRow> streamWeekly(int year, int month, int day, int duration);
}
