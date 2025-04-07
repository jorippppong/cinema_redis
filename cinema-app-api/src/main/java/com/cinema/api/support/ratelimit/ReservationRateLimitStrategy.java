package com.cinema.api.support.ratelimit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class ReservationRateLimitStrategy implements RateLimitStrategy {
    private final static String REQUEST_KEY = "request_ip";
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> redisScript;

    public ReservationRateLimitStrategy(StringRedisTemplate redisTemplate,
                                        @Qualifier("reservationRateLimitScript") DefaultRedisScript<Long> redisScript) {
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    @Override
    public boolean isAllowed(RateLimit rateLimit, String url, String ip) {
        String requestKey = generateRequestKey(url, ip);
        Long result = redisTemplate.execute(
                redisScript,
                Arrays.asList(requestKey),
                String.valueOf(rateLimit.limit()),
                TimeUnit.SECONDS.convert(rateLimit.ttl(), rateLimit.ttlTimeUnit())
        );

        return result != null && result != -1;
    }

    @Override
    public boolean supports(RateLimitType type) {
        return type == RateLimitType.RESERVATION;
    }

    private String generateRequestKey(String url, String ip) {
        return String.format("%s:%s:%s", REQUEST_KEY, url, ip);
    }
}
