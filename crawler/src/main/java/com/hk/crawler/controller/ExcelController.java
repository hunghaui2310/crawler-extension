package com.hk.crawler.controller;

import com.hk.crawler.model.Shop;
import com.hk.crawler.repository.IShopRepository;
import com.hk.crawler.utils.DateUtil;
import com.hk.crawler.utils.WriteToExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private IShopRepository shopRepository;

    @GetMapping("/shop")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=shop_" + DateUtil.getCurrentTimeStamp(null) + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Shop> listUsers = shopRepository.findAll();

        WriteToExcel excelExporter = new WriteToExcel(listUsers);

        excelExporter.export(response);
    }
}
