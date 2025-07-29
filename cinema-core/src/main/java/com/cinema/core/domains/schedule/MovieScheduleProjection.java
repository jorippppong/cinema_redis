package com.cinema.core.domains.schedule;

import com.cinema.core.domains.movie.Genre;
import com.cinema.core.domains.movie.Rating;

import java.time.LocalDateTime;

public record MovieScheduleProjection(
        Long movieId,
        String title,
        Rating rating,
        LocalDateTime releasedAt,
        String posterUrl,
        Integer runningTime,
        Genre genre,
        Long screenId,
        String screenName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
