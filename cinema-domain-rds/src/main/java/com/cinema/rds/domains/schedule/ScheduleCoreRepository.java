package com.cinema.rds.domains.schedule;

import com.cinema.core.domains.movie.Genre;
import com.cinema.core.domains.schedule.Schedule;
import com.cinema.core.domains.schedule.ScheduleRepository;
import com.cinema.rds.domains.movie.QMovieEntity;
import com.cinema.rds.domains.screen.QScreenEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleCoreRepository implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
    private final QMovieEntity movie = QMovieEntity.movieEntity;
    private final QScreenEntity screen = QScreenEntity.screenEntity;

    public ScheduleCoreRepository(ScheduleJpaRepository scheduleJpaRepository, JPAQueryFactory queryFactory) {
        this.scheduleJpaRepository = scheduleJpaRepository;
        this.queryFactory = queryFactory;
    }

    /**
     * 가장 최근 개봉한 영화 순으로 정렬한다.
     * 시간 시간이 빠른 것부터 정렬된다.
     *
     * @return
     */
    @Override
    public List<Schedule> getSchedule(String title, Genre genre) {
        LocalDateTime currentDate = LocalDate.of(2025, 1, 1) //LocalDate.now()
                .atStartOfDay();

        List<ScheduleEntity> scheduleEntities = queryFactory.selectFrom(schedule)
                .join(schedule.movie, movie)
                .fetchJoin()
                .join(schedule.screen, screen)
                .fetchJoin()
                .where(schedule.startAt.goe(currentDate), titleContains(title), genreEq(genre))
                .orderBy(movie.releasedAt.desc(), schedule.startAt.asc())
                .fetch();

        return scheduleEntities.stream()
                .map(ScheduleEntity::toSchedule)
                .toList();
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleJpaRepository.findById(id)
                .map(ScheduleEntity::toSchedule);
    }

    private BooleanExpression titleContains(String title) {
        return (title == null || title.isBlank()) ? null : movie.title.contains(title);
    }

    private BooleanExpression titleEq(String title) {
        return (title == null || title.isBlank()) ? null : movie.title.eq(title);
    }

    private BooleanExpression genreEq(Genre genre) {
        return genre == null ? null : movie.genre.eq(genre);
    }

}
