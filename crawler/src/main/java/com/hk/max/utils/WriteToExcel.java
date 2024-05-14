package com.hk.max.utils;

import com.hk.max.dto.ShopExcelDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WriteToExcel {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ShopExcelDTO> shops;

    public WriteToExcel(List<ShopExcelDTO> shops) {
        this.shops = shops;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Hải Dương_" + DateUtil.getCurrentTimeStamp(null));

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "ShopID", style);
        createCell(row, 1, "Username", style);
        createCell(row, 2, "Số điện thoại", style);
        createCell(row, 3, "Địa chỉ", style);
        createCell(row, 4, "Doanh thu từ", style);
        createCell(row, 5, "Doanh thu đến", style);
        createCell(row, 6, "Ngày tạo shop", style);
        createCell(row, 7, "Shop URL", style);
        createCell(row, 8, "Tổng số SP", style);
        createCell(row, 9, "Trạng thái", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (ShopExcelDTO shop : shops) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

//            createCell(row, columnCount++, rowCount - 1, style);
            createCell(row, columnCount++, shop.getShopid(), style);
            createCell(row, columnCount++, shop.getUsername(), style);
            createCell(row, columnCount++, shop.getPhoneNumber(), style);
            createCell(row, columnCount++, shop.getAddress(), style);
            createCell(row, columnCount++, shop.getTotalRevenueMin(), style);
            createCell(row, columnCount++, shop.getTotalRevenueMax(), style);
            createCell(row, columnCount++, shop.getShopCreateDate(), style);
            createCell(row, columnCount++, shop.getShopUrl(), style);
            createCell(row, columnCount++, shop.getTotalProduct(), style);
            createCell(row, columnCount++, shop.getIsActiveStr(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
