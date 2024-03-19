package com.hk.crawler.controller;

import com.hk.crawler.dto.ShopRawDTO;
import com.hk.crawler.model.Shop;
import com.hk.crawler.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    IShopService shopService;

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            return new ResponseEntity<>(shopService.getShopByPage(page, size), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-revenue")
    public ResponseEntity<?> getTotalRevenue(@RequestParam(name = "shopid") String shopid) {
        try {
            return new ResponseEntity<>(shopService.getRevenueByShop(shopid), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-info")
    public ResponseEntity<?> updateShopInfo(@RequestBody ShopRawDTO shopRawDTO) {
        try {
            return new ResponseEntity<>(shopService.updateShopInfo(shopRawDTO), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("")
//    public ResponseEntity<Shop> createTutorial(@RequestBody Shop shop) {
//        try {
//            Shop newShop = shopRepository.save(new Shop(shop.getName(), shop.getQuantity(), shop.getCategory()));
//            return new ResponseEntity<>(newShop, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
