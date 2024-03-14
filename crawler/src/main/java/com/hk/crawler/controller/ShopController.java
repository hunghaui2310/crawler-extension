package com.hk.crawler.controller;

import com.hk.crawler.model.Shop;
import com.hk.crawler.repository.IShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localost:8080")
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    IShopRepository shopRepository;

    @GetMapping("")
    public ResponseEntity<List<Shop>> getAll(@RequestParam(required = false) String title) {
        try {
            List<Shop> tutorials = new ArrayList<>();

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
