package com.hk.max.controller;

import com.hk.max.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private ICacheService cacheService;

    @GetMapping("")
    ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(cacheService.get("login"));
    }
}
