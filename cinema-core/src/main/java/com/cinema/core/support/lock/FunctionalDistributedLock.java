package com.cinema.core.support.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Component
public class FunctionalDistributedLock {
    private static final Logger log = LoggerFactory.getLogger(FunctionalDistributedLock.class);
    private final RedissonClient redissonClient;


    public FunctionalDistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 함수형 방식으로 분산 락을 처리
     *
     * @param key         락을 위한 고유한 키
     * @param waitTime    락 대기 시간
     * @param releaseTime 락의 임대 시간
     * @param timeUnit    시간 단위
     * @param task        락을 획득한 후 실행할 작업
     * @param <T>         작업 결과 타입
     * @return 작업 실행 결과
     * @throws Throwable 예외 발생 시
     */
    public <T> T withLock(String key, long waitTime, long releaseTime, TimeUnit timeUnit, Callable<T> task) throws Throwable {
        RLock rLock = redissonClient.getLock(key);

        boolean available = rLock.tryLock(waitTime, releaseTime, timeUnit);
        if (!available) {
            log.warn("Could not acquire lock for key : {}", key);
            throw new InterruptedException("Error while executing with functional distributed lock");
        }

        try {
            return task.call();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock for key : {}", key);
            }
        }
    }
}
