package com.hk.crawler.controller;

import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop-raw")
public class ShopRawDataController {

    @Autowired
    IShopRawDataRepository shopRawDataRepository;

    @Autowired
    private IShopService shopService;

    @PostMapping("")
    public ResponseEntity<ShopRawData> createShopRaw(@RequestBody ShopRawData shopRawData) {
        try {
            ShopRawData newShop = shopRawDataRepository.save(new ShopRawData(shopRawData.getData(), shopRawData.getUrl()));
            return new ResponseEntity<>(newShop, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkStatus(@RequestParam(name = "status", required = false) Boolean status) {
        try {
            shopService.saveFromRawShop();
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
