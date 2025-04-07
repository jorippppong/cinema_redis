package com.cinema.core.support.exception;

public enum CoreErrorCode {
    // user
    USER_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "USER-01", "존재하지 않는 사용자입니다."),

    // schedule
    SCHEDULE_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "SCHEDULE-01", "존재하지 않는 상영 스케줄 입니다."),

    // seat
    OVER_MAX_SEAT_SIZE(CoreErrorStatus.FORBIDDEN, "SEAT-01", "최대 예약 가능한 좌석 갯수인 5보다 많습니다."),
    SEAT_ALREADY_BOOKED(CoreErrorStatus.FORBIDDEN, "SEAT-02", "이미 예약이 완료된 좌석입니다."),
    SEAT_NOT_IN_SEQUENCE(CoreErrorStatus.FORBIDDEN, "SEAT-03", "연속된 좌석이 아닙니다."),
    SEAT_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "SEAT-04", "해당 상영관에 존재하지 않는 좌석입니다."),

    // rate limit
    TOO_MANY_REQUEST(CoreErrorStatus.TOO_MANY_REQUEST, "RATE_LIMIT-01", "요청이 너무 많습니다. 나중에 다시 시도해주세요."),
    RATE_LIMIT_TYPE_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "RATE_LIMIT-02", "존재하지 않는 rate limit 전략입니다.");


    private final CoreErrorStatus httpStatus;
    private final String code;
    private final String message;

    CoreErrorCode(CoreErrorStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public CoreErrorStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
