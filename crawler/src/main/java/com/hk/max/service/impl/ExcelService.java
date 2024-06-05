package com.hk.max.service.impl;

import com.hk.max.dto.ShopExcelDTO;
import com.hk.max.model.Category;
import com.hk.max.repository.ICategoryRepository;
import com.hk.max.service.IExcelService;
import com.hk.max.service.IShopService;
import com.hk.max.utils.AppUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService implements IExcelService {

    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//    private List<ShopExcelDTO> shops;
//    private List<Category> categories;

    private final ICategoryRepository categoryRepository;

    private final IShopService shopService;

    @Autowired
    public ExcelService(ICategoryRepository categoryRepository, IShopService shopService) {
        this.categoryRepository = categoryRepository;
        this.shopService = shopService;
    }

    public void writeHeaderLine() {
        this.workbook = new XSSFWorkbook();
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            String replaced = category.getName().replaceAll("[\\\\/*\\[\\]:?]", "_");
//            String name = replaced;
//            int sheetIndex = workbook.getSheetIndex(replaced);
            XSSFSheet sheet = workbook.createSheet(category.getCatid() + "-" + AppUtils.removeVietnameseDiacritics(replaced));;
//            if (sheetIndex == -1) {
//                sheet = workbook.createSheet(replaced);
//            } else {
//                sheet = workbook.createSheet(new Date().getTime() + replaced);
//            }
            Row row = sheet.createRow(0);

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(14);
            style.setFont(font);

            createCell(row, 0, "STT", style, sheet);
            createCell(row, 1, "ShopID", style, sheet);
            createCell(row, 2, "Username", style, sheet);
            createCell(row, 3, "Số điện thoại", style, sheet);
            createCell(row, 4, "Địa chỉ", style, sheet);
            createCell(row, 5, "Doanh thu từ", style, sheet);
            createCell(row, 6, "Doanh thu đến", style, sheet);
            createCell(row, 7, "Ngày tạo shop", style, sheet);
            createCell(row, 8, "Shop URL", style, sheet);

            int rowCount = 1;
            CellStyle style2 = workbook.createCellStyle();
            XSSFFont font2 = workbook.createFont();
            font2.setFontHeight(14);
            style2.setFont(font2);

            List<ShopExcelDTO> shops = shopService.getExcelData(category.getCatid().toString());

            for (ShopExcelDTO shop : shops) {
                Row rowData = sheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(rowData, columnCount++, rowCount - 1, style2, sheet);
                createCell(rowData, columnCount++, shop.getShopid(), style2, sheet);
                createCell(rowData, columnCount++, shop.getUsername(), style2, sheet);
                createCell(rowData, columnCount++, shop.getPhoneNumber(), style2, sheet);
                createCell(rowData, columnCount++, shop.getAddress(), style2, sheet);
                createCell(rowData, columnCount++, shop.getTotalRevenueMin(), style2, sheet);
                createCell(rowData, columnCount++, shop.getTotalRevenueMax(), style2, sheet);
                createCell(rowData, columnCount++, shop.getShopCreateDate(), style2, sheet);
                createCell(rowData, columnCount++, shop.getShopUrl(), style2, sheet);
            }

//            rowCount++;
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
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
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
//        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
