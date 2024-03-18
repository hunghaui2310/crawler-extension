package com.hk.crawler.controller;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopProductRawDataRepository;
import com.hk.crawler.repository.IShopRawDataRepository;
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
    private IShopProductRawDataRepository shopProductRawDataRepository;

    @Autowired
    private IShopService shopService;

    @PostMapping("")
    public ResponseEntity<?> createShopRaw(@RequestBody ProductRawData productRawData) {
        try {
            List<ShopProductRawData> productRawDataList = shopProductRawDataRepository.findAllByUrl(productRawData.getUrl());
            ShopProductRawData newShop;
            if (productRawDataList.size() > 0) {
                newShop = productRawDataList.get(0);
                newShop.setData(productRawData.getData());
            } else {
                newShop = new ShopProductRawData(productRawData.getData(), productRawData.getUrl());
            }
            shopProductRawDataRepository.save(newShop);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
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
