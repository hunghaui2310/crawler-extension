package com.hk.max.utils;

import com.hk.max.dto.ShopExcelDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
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

        createCell(row, 0, "STT", style);
        createCell(row, 1, "ShopID", style);
        createCell(row, 2, "Username", style);
        createCell(row, 3, "Số điện thoại", style);
        createCell(row, 4, "Địa chỉ", style);
        createCell(row, 5, "Doanh thu", style);
        createCell(row, 6, "Ngày tạo shop", style);
        createCell(row, 7, "Shop URL", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
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

            createCell(row, columnCount++, rowCount - 1, style);
            createCell(row, columnCount++, shop.getShopid(), style);
            createCell(row, columnCount++, shop.getUsername(), style);
            createCell(row, columnCount++, shop.getPhoneNumber(), style);
            createCell(row, columnCount++, shop.getAddress(), style);
            createCell(row, columnCount++, shop.getTotalRevenue(), style);
            createCell(row, columnCount++, shop.getShopCreateDate(), style);
            createCell(row, columnCount++, shop.getShopUrl(), style);
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
