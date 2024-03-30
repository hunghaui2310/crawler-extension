package com.hk.max.service;

import com.hk.max.utils.AppUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppService {

    @Value("${spring.app.root.dir}")
    private String appRootDir;

    @Autowired
    private ICacheService cacheService;

    @PostConstruct
    void startUp() {
        log.info("Starting open Google Chrome in incognito mode" + this.appRootDir);
//        String pythonCmd = "python3 " + this.appRootDir + "/autorun.py";
//        boolean result = AppUtils.RunCmd(pythonCmd);
//        if (result) {
//            cacheService.set("login", "1");
//        }
    }
}
