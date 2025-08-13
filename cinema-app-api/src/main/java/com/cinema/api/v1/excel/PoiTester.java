package com.cinema.api.v1.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import java.io.FileOutputStream;

public class PoiTester {
    private final int ROW_CNT = 10000;
    private final int COL_CNT = 100;
    private Workbook workbook;
    private String fileName;

    public PoiTester(Workbook workbook, String fileName) {
        this.workbook = workbook;
        this.fileName = fileName;
    }

    public void test() {
        long beginTime = System.currentTimeMillis();
        Workbook wb = workbook;
        Sheet sheet = wb.createSheet();

        for (int rownum = 0; rownum < ROW_CNT; rownum++) {
            Row row = sheet.createRow(rownum);
            for (int cellNum = 0; cellNum < COL_CNT; cellNum++) {
                Cell cell = row.createCell(cellNum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(fileName + ".xlsx");
            wb.write(fos);

            long endTime = System.currentTimeMillis();
            long totalSec = (endTime - beginTime) / 1000;
            System.out.println("Total time taken: " + totalSec + " seconds");

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
