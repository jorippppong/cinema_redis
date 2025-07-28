package com.cinema.core.domains.schedule;

import java.time.LocalDateTime;

import com.cinema.core.domains.movie.Genre;
import com.cinema.core.domains.movie.Rating;

public record Schedule(
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
