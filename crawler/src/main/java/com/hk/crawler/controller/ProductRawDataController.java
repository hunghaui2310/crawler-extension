package com.hk.crawler.controller;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.repository.IProductRawDataRepository;
import com.hk.crawler.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-raw")
public class ProductRawDataController {

    @Autowired
    IProductRawDataRepository productRawDataRepository;

    @Autowired
    private IProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductRawData> createShopRaw(@RequestBody ProductRawData productRawData) {
        try {
            ProductRawData newProduct = productRawDataRepository.save(new ProductRawData(productRawData.getData(), productRawData.getUrl()));
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
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
