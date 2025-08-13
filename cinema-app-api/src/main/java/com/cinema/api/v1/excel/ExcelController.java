package com.cinema.api.v1.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/excel")
public class ExcelController {
    private final ExcelServiceXSSService excelService;

    public ExcelController(ExcelServiceXSSService excelService) {
        this.excelService = excelService;
    }

    @GetMapping()
    public void exportSchedule(HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("schedules.xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        excelService.exportScheduleExcel(response.getOutputStream());
    }
}
