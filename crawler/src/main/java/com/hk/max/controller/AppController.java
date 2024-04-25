package com.hk.max.controller;

import com.hk.max.concurrence.AutoRunTask;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class AppController {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @GetMapping("/auto-run")
    public ResponseEntity<?> autoRunApp() {
        try {
            AutoRunTask sc = applicationContext.getBean(AutoRunTask.class);
            Thread thread = new Thread(sc);
            thread.run();
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
