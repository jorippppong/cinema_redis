package com.cinema.api.v1.excel;

import com.cinema.core.domains.excel.ScheduleRow;
import com.cinema.core.domains.schedule.ScheduleRepository;
import jakarta.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class ExcelServiceXSSService {
    private final ScheduleRepository scheduleRepository;

    public ExcelServiceXSSService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * BlockingQueue 없이 단일 스레드에서 스트림을 바로 엑셀로 기록
     */
    @Transactional(readOnly = true)
    public void exportScheduleExcel(ServletOutputStream outputStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Schedules");
            AtomicInteger rowCount = new AtomicInteger(0);

            try (Stream<ScheduleRow> stream = scheduleRepository.streamWeekly(2025, 8, 1, 5)) {
                stream.forEach(schedule -> {
                    Row row = sheet.createRow(rowCount.getAndIncrement());
                    row.createCell(0).setCellValue(schedule.getId());
                    row.createCell(1).setCellValue(schedule.getStartAt() != null ? schedule.getStartAt().toString() : "");
                    row.createCell(2).setCellValue(schedule.getEndAt() != null ? schedule.getEndAt().toString() : "");
                });
            }

            workbook.write(outputStream);
        }
    }

}
