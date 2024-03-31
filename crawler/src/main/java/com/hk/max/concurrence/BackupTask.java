package com.hk.max.concurrence;

import com.hk.max.utils.AppUtils;
import com.hk.max.utils.DateUtil;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class BackupTask {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PreDestroy
    public void backupDataBeforeShutdown() {
        log.info("Called to backup data before shutdown");
        Set<String> collectionNames = mongoTemplate.getCollectionNames();

        for (String collectionName : collectionNames) {
            List<Object> collectionData = mongoTemplate.findAll(Object.class, collectionName);

            writeCollectionDataToFile(collectionName, collectionData);
        }
    }

    private void writeCollectionDataToFile(String collectionName, List<Object> collectionData) {
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String rootDir = currentWorkingDir.toString();
        String today = DateUtil.getCurrentTimeStamp(null);
        String dir = rootDir + "/backup/" + today;
        AppUtils.createFolderIfNotExist(dir);

        String fileName = dir + "/" + collectionName + ".json";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            // Write collection data as JSON to the file
            for (Object data : collectionData) {
                fileWriter.write(data.toString());
                fileWriter.write("\n");
            }
            log.info("Exported collection '" + collectionName + "' to " + fileName);
        } catch (IOException e) {
            log.error("Error occurred while writing collection '" + collectionName + "' to file: " + e.getMessage());
        }
    }
}
