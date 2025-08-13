package com.cinema.core.domains.excel;

import java.time.LocalDateTime;

public class ScheduleRow {
    private final Long id;
    private final String startAt;
    private final String endAt;

    public ScheduleRow(Long id) {
        this.id = id;
        this.startAt = LocalDateTime.now().toString();
        this.endAt = LocalDateTime.now().toString();
    }

    public ScheduleRow(Long id, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.startAt = startAt.toString();
        this.endAt = endAt.toString();
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }
}
