package com.cinema.api.support.ratelimit;

import com.cinema.core.support.exception.CoreErrorCode;
import com.cinema.core.support.exception.CoreException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class LimitRequestAop {
    private final List<RateLimitStrategy> strategies;
    private final HttpServletRequest request;

    public LimitRequestAop(List<RateLimitStrategy> strategyList, HttpServletRequest request) {
        this.strategies = strategyList;
        this.request = request;
    }

    @Around("@annotation(com.cinema.api.support.ratelimit.LimitRequest)")
    public Object handleRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LimitRequest limitRequest = method.getAnnotation(LimitRequest.class);

        RateLimitType type = limitRequest.type();
        RateLimitStrategy strategy = strategies.stream()
                .filter(s -> s.supports(type))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.RATE_LIMIT_TYPE_NOT_FOUND));

        boolean allowed = strategy.isAllowed(limitRequest, getRequestUrl(), getClientIp());

        if (!allowed) {
            throw new CoreException(CoreErrorCode.TOO_MANY_REQUEST);
        }
        return joinPoint.proceed();
    }

    private String getClientIp() {
        return request.getRemoteAddr();  // 프록시 서버는 고려 X
    }

    private String getRequestUrl() {
        return request.getRequestURI();
    }
}
