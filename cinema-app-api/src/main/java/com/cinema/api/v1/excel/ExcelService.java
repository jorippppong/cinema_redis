package com.cinema.api.v1.excel;

import com.cinema.core.domains.excel.ScheduleRow;
import com.cinema.core.domains.schedule.ScheduleRepository;
import jakarta.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class ExcelService {
    private final ScheduleRepository scheduleRepository;
    private final int QUEUE_SIZE = 1000;
    private final ScheduleRow POISON_PILL = new ScheduleRow(-1L);

    public ExcelService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 1) exportScheduleExcel() 메서드 전체에 @Transactional(readOnly = true)를 걸고
     * 2) 그리고 producer 스레드에서 스트림 처리하지 말고, 메인 스레드에서 스트림을 읽으면서 BlockingQueue에 넣는 방식으로 변경
     * 즉, 트랜잭션 범위 안에서 스트림을 소비하도록 구조를 바꿔야 합니다.
     * Producer 스레드를 따로 둘 경우 트랜잭션 범위 밖으로 벗어나면서 스트림처리가 안됨
     */
    @Transactional(readOnly = true)
    public void exportScheduleExcel(ServletOutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000)) {
            Sheet sheet = workbook.createSheet("Schedules"); // Sheet 없어서 새로 생성
            BlockingQueue<ScheduleRow> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
            Thread consumer = getConsumer(queue, sheet);

            // producer 역할을 현재 스레드에서 처리 (트랜잭션 유지)
            try (Stream<ScheduleRow> stream = scheduleRepository.streamWeekly(2025, 8, 1, 5)) {
                stream.forEach(schedule -> {
                    try {
                        queue.put(schedule);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                queue.put(POISON_PILL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // consumer 종료 대기 및 엑셀 작성 완료
            try {
                consumer.join();
                workbook.write(outputStream);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                workbook.dispose();
            }
        }
    }

    private Thread getConsumer(BlockingQueue<ScheduleRow> queue, Sheet sheet) {
        AtomicInteger rowCount = new AtomicInteger(0);

        // consumer 스레드는 엑셀에 기록만 담당
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    ScheduleRow schedule = queue.take();
                    if (schedule == POISON_PILL) break;

                    Row row = sheet.createRow(rowCount.getAndIncrement());
                    row.createCell(0).setCellValue(schedule.getId());
                    row.createCell(1).setCellValue(schedule.getStartAt() != null ? schedule.getStartAt() : "");
                    row.createCell(2).setCellValue(schedule.getEndAt() != null ? schedule.getEndAt() : "");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer-thread");

        consumer.start();
        return consumer;
    }
}
