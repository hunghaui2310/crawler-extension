package com.hk.max.controller;

import com.hk.max.dto.ShopExcelDTO;
import com.hk.max.service.IShopService;
import com.hk.max.utils.DateUtil;
import com.hk.max.utils.WriteToExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private IShopService shopService;

    @GetMapping("/shop")
    public void exportToExcel(@RequestParam(name = "catid") String catid, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=shop_" + DateUtil.getCurrentTimeStamp(null) + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<ShopExcelDTO> listShop = shopService.getExcelData(catid);

        WriteToExcel excelExporter = new WriteToExcel(listShop);

        excelExporter.export(response);
    }
}
