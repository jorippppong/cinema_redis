package com.cinema.rds.domains.schedule;

import java.time.LocalDateTime;

import com.cinema.core.schedule.Schedule;
import com.cinema.rds.domains.common.BaseEntity;
import com.cinema.rds.domains.movie.MovieEntity;
import com.cinema.rds.domains.screen.ScreenEntity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "schedule")
public class ScheduleEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime startAt;

	private LocalDateTime endAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private MovieEntity movie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ScreenEntity screen;

	public Schedule toSchedule() {
		return new Schedule(movie.getId(), movie.getTitle(), movie.getRating(), movie.getReleasedAt(),
			movie.getPosterUrl(),
			movie.getRunningTime(), movie.getGenre(), screen.getId(), screen.getName(), startAt, endAt);
	}

}
