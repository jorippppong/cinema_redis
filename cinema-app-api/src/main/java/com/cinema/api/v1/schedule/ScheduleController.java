package com.cinema.api.v1.schedule;

import com.cinema.api.support.ratelimit.RateLimit;
import com.cinema.api.support.ratelimit.RateLimitType;
import com.cinema.api.v1.schedule.dto.MovieWithSchedule;
import com.cinema.core.domains.movie.Genre;
import com.cinema.core.domains.schedule.Schedule;
import com.cinema.core.domains.schedule.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @RateLimit(
            type = RateLimitType.SEARCH,
            limit = 50,
            ttl = 1,
            ttlTimeUnit = TimeUnit.MINUTES,
            banTime = 1,
            banTimeUnit = TimeUnit.HOURS
    )
    @GetMapping()
    public ResponseEntity<List<MovieWithSchedule>> getOngoingSchedule(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Genre genre
    ) {
        isParamsValid(title, genre);
        List<Schedule> response = scheduleService.getOngoingSchedule(title, genre);
        return ResponseEntity.ok(MovieWithSchedule.from(response));
    }

    private void isParamsValid(String title, Genre genre) {
        if (title != null && title.length() > 255) {
            throw new IllegalArgumentException("영화 제목은 255자를 넘길 수 없습니다.");
        }
    }
}
