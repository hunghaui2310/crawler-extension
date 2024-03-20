package com.hk.crawler.controller;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopProductRawDataRepository;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.service.IRawDataService;
import com.hk.crawler.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop-product-raw")
public class ShopProductRawDataController {

    @Autowired
    private IRawDataService rawDataService;

    @Autowired
    private IShopService shopService;

    @PostMapping("")
    public ResponseEntity<?> createShopRaw(@RequestBody ShopProductRawData shopProductRawData) {
        try {
            return new ResponseEntity<>(rawDataService.saveToShopProductRawData(shopProductRawData), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkStatus(@RequestParam(name = "status", required = false) Boolean status) {
        try {
            if (status) {
                shopService.saveFromShopProductRawData();
            }
            return new ResponseEntity<>(status, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
