package com.hk.crawler.controller;

import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopRawDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/raw")
public class ShopRawDataController {

    @Autowired
    IShopRawDataRepository shopRawDataRepository;

    @PostMapping("")
    public ResponseEntity<ShopRawData> createShopRaw(@RequestBody ShopRawData shopRawData) {
        try {
            ShopRawData newShop = shopRawDataRepository.save(new ShopRawData(shopRawData.getData(), shopRawData.getUrl()));
            return new ResponseEntity<>(newShop, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
