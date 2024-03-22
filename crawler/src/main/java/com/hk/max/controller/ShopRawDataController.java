package com.hk.max.controller;

import com.hk.max.model.ShopRawData;
import com.hk.max.service.IRawDataService;
import com.hk.max.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop-raw")
public class ShopRawDataController {

    @Autowired
    IRawDataService rawDataService;

    @Autowired
    private IShopService shopService;

    @PostMapping("")
    public ResponseEntity<?> createShopRaw(@RequestBody ShopRawData shopRawData) {
        try {
            return new ResponseEntity<>(rawDataService.saveToShopRawData(shopRawData), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkStatus(@RequestParam(name = "status", required = false) Boolean status) {
        try {
            if (status) {
                shopService.saveFromRawShop();
            }
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
