package com.cinema.core.domains.schedule;

import com.cinema.core.domains.excel.ScheduleRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

    @Query("SELECT new com.cinema.core.domains.excel.ScheduleRow(s.id, s.startAt, s.endAt) FROM ScheduleEntity s WHERE s.startAt >= :start AND s.startAt < :end")
    Stream<ScheduleRow> streamByStartAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
