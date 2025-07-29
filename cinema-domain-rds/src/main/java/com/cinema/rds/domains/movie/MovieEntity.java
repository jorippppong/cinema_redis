package com.cinema.rds.domains.movie;

import com.cinema.core.domains.movie.Genre;
import com.cinema.core.domains.movie.Rating;
import com.cinema.rds.domains.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie",
        indexes = {
                @Index(name = "idx_movie_genre_title", columnList = "genre, title")
        }
)
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
