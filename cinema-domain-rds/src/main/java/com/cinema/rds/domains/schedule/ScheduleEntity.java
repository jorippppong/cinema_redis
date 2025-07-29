package com.cinema.rds.domains.schedule;

import com.cinema.core.domains.schedule.MovieScheduleProjection;
import com.cinema.rds.domains.common.BaseEntity;
import com.cinema.rds.domains.movie.MovieEntity;
import com.cinema.rds.domains.screen.ScreenEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule",
        indexes = {
                @Index(name = "idx_schedule_movie_id_start_at", columnList = "movie_id, start_at")
        }
)
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

    public MovieScheduleProjection toSchedule() {
        return new MovieScheduleProjection(movie.getId(), movie.getTitle(), movie.getRating(), movie.getReleasedAt(),
                movie.getPosterUrl(),
                movie.getRunningTime(), movie.getGenre(), screen.getId(), screen.getName(), startAt, endAt);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }
}
