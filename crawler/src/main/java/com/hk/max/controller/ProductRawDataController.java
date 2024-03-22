package com.hk.max.controller;

import com.hk.max.model.ProductRawData;
import com.hk.max.service.IProductService;
import com.hk.max.service.IRawDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-raw")
public class ProductRawDataController {

    @Autowired
    IRawDataService rawDataService;

    @Autowired
    private IProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createShopRaw(@RequestBody ProductRawData productRawData) {
        try {
            return new ResponseEntity<>(rawDataService.saveToProductRawData(productRawData), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkStatus(@RequestParam(name = "status", required = false) Boolean status) {
        try {
            if (status) {
                productService.saveFromRawProduct();
            }
            return new ResponseEntity<>(status, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
