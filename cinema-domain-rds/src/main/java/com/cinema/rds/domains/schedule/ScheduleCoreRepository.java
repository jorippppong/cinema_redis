package com.cinema.rds.domains.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cinema.core.schedule.Schedule;
import com.cinema.core.schedule.ScheduleRepository;
import com.cinema.rds.domains.movie.QMovieEntity;
import com.cinema.rds.domains.screen.QScreenEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
	 * 가장 최근 개봉한 영화들이 맨 위에 올라간다.
	 * 시간 시간이 빠른 것부터 정렬된다.
	 * @return
	 */
	@Override
	public List<Schedule> getSchedule() {
		LocalDateTime currentDate = LocalDate.now()
			.atStartOfDay();

		List<ScheduleEntity> scheduleEntities = queryFactory.selectFrom(schedule)
			.join(schedule.movie, movie)
			.fetchJoin()
			.join(schedule.screen, screen)
			.fetchJoin()
			.where(schedule.startAt.goe(currentDate))
			.orderBy(movie.releasedAt.desc(), schedule.startAt.asc())
			.fetch();

		return scheduleEntities.stream()
			.map(ScheduleEntity::toSchedule)
			.toList();
	}
}
