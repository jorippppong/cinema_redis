package com.cinema.api.support.ratelimit;

import org.springframework.stereotype.Component;

@Component
public interface RateLimitStrategy {
    boolean isAllowed(LimitRequest limitRequest, String url, String ip);

    boolean supports(RateLimitType type);
}
