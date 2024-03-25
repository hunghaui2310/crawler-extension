package com.hk.max.controller;

import com.hk.max.dto.ShopRawDTO;
import com.hk.max.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    IShopService shopService;

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(required = false, defaultValue = "false", name = "isCrawled") boolean isCrawled,
                                    @RequestParam(required = false, defaultValue = "", name = "catid") String catid,
                                    @RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            return new ResponseEntity<>(shopService.getShopByPage(isCrawled, catid, page, size), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-revenue")
    public ResponseEntity<?> getTotalRevenue(@RequestParam(name = "shopid") String shopid) {
        try {
            return new ResponseEntity<>(shopService.getRevenueByShop(shopid, false), HttpStatus.OK);
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

    @GetMapping("/isCrawlDone")
    public ResponseEntity<?> updateShopInfo(@RequestParam(name = "shopid") String shopid,
                                @RequestParam(name = "isDone", defaultValue = "false") boolean isDone) {
        try {
            if (isDone) {
                shopService.updateShopLastCrawlAt(shopid);
            }
            return new ResponseEntity<>(isDone, HttpStatus.OK);
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
