package com.hk.max.concurrence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShopProductRawTask implements Runnable {

    public ShopProductRawTask() {
    }

    @Override
    public void run() {
        log.info("Thread ShopProductRawTask is running" + Thread.currentThread().getName());


    }
}
