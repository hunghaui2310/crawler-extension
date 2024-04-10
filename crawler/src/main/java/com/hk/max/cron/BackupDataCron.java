package com.hk.max.cron;

import com.hk.max.concurrence.BackupTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BackupDataCron {

    @Autowired
    private BackupTask backupTask;

//    @Scheduled(cron = "0 0 3 * * *")
//    public void backupDataInEveryDay() throws IOException, InterruptedException {
//        this.backupTask.backupData();
//    }
}
