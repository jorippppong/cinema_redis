package com.cinema.api.support.ratelimit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class SearchRateLimitStrategy implements RateLimitStrategy {
    private final static String REQUEST_KEY = "request_ip";
    private final static String BAN_KEY = "banned_ip";
    private final RedisTemplate<String, Object> redisTemplate;
    private final DefaultRedisScript<Long> redisScript;

    public SearchRateLimitStrategy(
            RedisTemplate<String, Object> redisTemplate,
            @Qualifier("searchRateLimitScript") DefaultRedisScript<Long> redisScript) {
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    @Override
    public boolean isAllowed(RateLimit rateLimit, String url, String ip) {
        String requestKey = generateRequestKey(url, ip);
        String banKey = generateBanKey(url, ip);

        Long result = redisTemplate.execute(
                redisScript,
                Arrays.asList(requestKey, banKey),
                rateLimit.limit(),
                TimeUnit.SECONDS.convert(rateLimit.ttl(), rateLimit.ttlTimeUnit()),
                TimeUnit.SECONDS.convert(rateLimit.banTime(), rateLimit.banTimeUnit())
        );
        // System.out.println(result);

        return result != null && result != -1;
    }

    @Override
    public boolean supports(RateLimitType type) {
        return type == RateLimitType.SEARCH;
    }

    private String generateRequestKey(String url, String ip) {
        return String.format("%s:%s:%s", REQUEST_KEY, url, ip);
    }

    private String generateBanKey(String url, String ip) {
        return String.format("%s:%s:%s", BAN_KEY, url, ip);
    }
}
