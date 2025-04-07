package com.cinema.api.support.ratelimit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class SearchRateLimitStrategy implements RateLimitStrategy {
    private final static String REQUEST_KEY = "request_ip";
    private final static String BAN_KEY = "banned_ip";
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> searchRateLimitScript;

    public SearchRateLimitStrategy(StringRedisTemplate redisTemplate,
                                   @Qualifier("searchRateLimitScript") DefaultRedisScript<Long> searchRateLimitScript) {
        this.redisTemplate = redisTemplate;
        this.searchRateLimitScript = searchRateLimitScript;
    }

    @Override
    public boolean isAllowed(LimitRequest limitRequest, String url, String ip) {
        String requestKey = generateRequestKey(url, ip);
        String banKey = generateBanKey(url, ip);

        Long result = redisTemplate.execute(
                searchRateLimitScript,
                Arrays.asList(requestKey, banKey),
                String.valueOf(limitRequest.limit()),
                TimeUnit.SECONDS.convert(limitRequest.ttl(), limitRequest.ttlTimeUnit()),
                TimeUnit.SECONDS.convert(limitRequest.banTime(), limitRequest.banTimeUnit())
        );

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
