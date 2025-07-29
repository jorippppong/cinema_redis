package com.cinema.api.v1.schedule.dto;

import com.cinema.core.domains.schedule.MovieScheduleProjection;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record MovieWithSchedule(
        Long movieId,
        String title,
        String Rating,
        LocalDateTime releasedAt,
        String posterUrl,
        Integer runningTime,
        String genre,
        List<ScreenDetail> screens
) {
    public static List<MovieWithSchedule> from(List<MovieScheduleProjection> movieScheduleProjections) {
        return movieScheduleProjections.stream()
                .collect(Collectors.groupingBy(MovieScheduleProjection::movieId))
                .entrySet()
                .stream()
                .map(entry -> {
                    Long movieId = entry.getKey();
                    List<MovieScheduleProjection> groupByMovie = entry.getValue();
                    MovieScheduleProjection movieInfo = groupByMovie.get(0);
                    List<ScreenDetail> screenDetails = groupByMovie.stream()
                            .collect(Collectors.groupingBy(MovieScheduleProjection::screenId))
                            .entrySet()
                            .stream()
                            .map(screenEntry -> {
                                Long screenId = screenEntry.getKey();
                                String screenName = screenEntry.getValue()
                                        .get(0)
                                        .screenName();
                                List<ScheduleDetail> scheduleDetails = screenEntry.getValue()
                                        .stream()
                                        .sorted(Comparator.comparing(MovieScheduleProjection::startTime))
                                        .map(dto -> new ScheduleDetail(dto.startTime(), dto.endTime()))
                                        .toList();
                                return new ScreenDetail(screenId, screenName, scheduleDetails);
                            })
                            .toList();

                    return new MovieWithSchedule(
                            movieId,
                            movieInfo.title(),
                            movieInfo.rating()
                                    .toString(),
                            movieInfo.releasedAt(),
                            movieInfo.posterUrl(),
                            movieInfo.runningTime(),
                            movieInfo.genre()
                                    .toString(),
                            screenDetails
                    );
                })
                .sorted(Comparator.comparing(MovieWithSchedule::releasedAt)
                        .reversed())
                .toList();
    }

}
