package com.cinema.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RateLimitScriptConfig {
    @Bean(name = "searchRateLimitScript")
    public DefaultRedisScript<Long> searchRateLimit() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("scripts/rate-limit-search.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean(name = "reservationRateLimitScript")
    public DefaultRedisScript<Long> reservationRateLimit() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("scripts/rate-limit-reservation.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
