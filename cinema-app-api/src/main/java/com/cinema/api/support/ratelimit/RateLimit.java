package com.cinema.api.support.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 조회, 예약 등 제한 유형
     */
    RateLimitType type();

    /**
     * 최대 호출 가능 횟수
     */
    int limit();

    /**
     * 제한 시간
     */
    int ttl();

    /**
     * 제한 시간 단위
     */
    TimeUnit ttlTimeUnit() default TimeUnit.SECONDS;

    /**
     * 금지 시간
     */
    int banTime() default 0;

    /**
     * 금지 시간 단위
     */
    TimeUnit banTimeUnit() default TimeUnit.SECONDS;

    // ip, url은 따로 처리
}
