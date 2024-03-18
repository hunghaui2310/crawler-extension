package com.hk.crawler.controller;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop-raw")
public class ShopRawDataController {

    @Autowired
    IShopRawDataRepository shopRawDataRepository;

    @Autowired
    private IShopService shopService;

    @PostMapping("")
    public ResponseEntity<?> createShopRaw(@RequestBody ShopRawData shopRawData) {
        try {
            List<ShopRawData> productRawDataList = shopRawDataRepository.findAllByUrl(shopRawData.getUrl());
            ShopRawData newShop;
            if (productRawDataList.size() > 0) {
                newShop = productRawDataList.get(0);
                newShop.setData(shopRawData.getData());
            } else {
                newShop = new ShopRawData(shopRawData.getData(), shopRawData.getUrl());
            }
            shopRawDataRepository.save(newShop);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
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
