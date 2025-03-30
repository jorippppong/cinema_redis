package com.cinema.rds.domains.movie;

import java.time.LocalDateTime;

import com.cinema.core.movie.Genre;
import com.cinema.core.movie.Rating;
import com.cinema.rds.domains.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie")
public class MovieEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Enumerated(EnumType.STRING)
	private Rating rating;

	private LocalDateTime releasedAt;

	private String posterUrl;

	private Integer runningTime;

	@Enumerated(EnumType.STRING)
	private Genre genre;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Rating getRating() {
		return rating;
	}

	public LocalDateTime getReleasedAt() {
		return releasedAt;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public Integer getRunningTime() {
		return runningTime;
	}

	public Genre getGenre() {
		return genre;
	}
}
