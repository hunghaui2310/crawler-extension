package com.hk.crawler.controller;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.repository.IProductRawDataRepository;
import com.hk.crawler.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-raw")
public class ProductRawDataController {

    @Autowired
    IProductRawDataRepository productRawDataRepository;

    @Autowired
    private IProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createShopRaw(@RequestBody ProductRawData productRawData) {
        try {
            List<ProductRawData> productRawDataList = productRawDataRepository.findAllByUrl(productRawData.getUrl());
            ProductRawData newProduct;
            if (productRawDataList.size() > 0) {
                newProduct = productRawDataList.get(0);
                newProduct.setData(productRawData.getData());
            } else {
                newProduct = new ProductRawData(productRawData.getData(), productRawData.getUrl());
            }
            productRawDataRepository.save(newProduct);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
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
