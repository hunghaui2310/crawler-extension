package com.hk.max.concurrence;

import com.hk.max.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRunTask implements Runnable {

//    @Value("${spring.app.dir}")
//    private String appDir;

    @Override
    public void run() {
//        log.info("Starting auto run. Dir: " + this.appDir);

//        String pythonCmd = this.appDir;
//        AppUtils.RunCmd(pythonCmd);
//        if (result) {
//            log.info("Log from python file: " + result);
//        }
    }
}
