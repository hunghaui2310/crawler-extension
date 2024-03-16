package com.hk.crawler.controller;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.repository.IShopProductRawDataRepository;
import com.hk.crawler.repository.IShopRawDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop-product-raw")
public class ShopProductRawDataController {

    @Autowired
    private IShopProductRawDataRepository shopProductRawDataRepository;

    @PostMapping("")
    public ResponseEntity<ShopProductRawData> createShopRaw(@RequestBody ProductRawData productRawData) {
        try {
            ShopProductRawData newProduct = shopProductRawDataRepository.save(new ShopProductRawData(productRawData.getData(), productRawData.getUrl()));
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
