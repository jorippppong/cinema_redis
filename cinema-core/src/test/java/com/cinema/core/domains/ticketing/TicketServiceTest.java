package com.cinema.core.domains.ticketing;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
class TicketServiceTest {
	@Autowired
	TicketService ticketService;

	@Test
	@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void concurrentTest() throws InterruptedException {
		// given
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		Long scheduleId = 1L;
		Set<String> seats = Set.of("A1", "A2", "A3");
		List<Long> userIds = List.of(
			1L, 2L, 3L, 4L, 5L,
			6L, 7L, 8L, 9L, 10L
		);
		AtomicInteger count = new AtomicInteger(0);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					Long userId = userIds.get(ThreadLocalRandom.current()
						.nextInt(userIds.size()));
					CreateTicketingCommand command = new CreateTicketingCommand(userId, scheduleId, seats);
					ticketService.createTicketing(command);
					count.getAndIncrement();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);

		// then
		Assertions.assertThat(count.get())
			.isEqualTo(1);
	}

}
