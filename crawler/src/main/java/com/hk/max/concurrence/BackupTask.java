package com.hk.max.concurrence;

import com.hk.max.utils.AppUtils;
import com.hk.max.utils.DateUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

@Component
@Slf4j
public class BackupTask {

    @Value("${spring.app.backup_dir}")
    private String backupDir;

    @Value("${spring.data.mongodb.uri}")
    private String URL_MONGDO;

    @PostConstruct
    public void backupDataAtStartup() {
//        this.backupData();
    }

    @PreDestroy
    public void backupDataBeforeShutdown(){
//        this.backupData();
    }

    public void backupData() {
        try (MongoClient mongoClient = MongoClients.create(this.URL_MONGDO)) {
            MongoDatabase database = mongoClient.getDatabase("crawler");

            // Get collection names
            List<String> collectionNames = new ArrayList<>();
            for (String name : database.listCollectionNames()) {
                collectionNames.add(name);
            }

            // Export each collection
            for (String collectionName : collectionNames) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                exportCollection(collectionName, collection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void exportCollection(String collectionName, MongoCollection<Document> collection) {
        String today = DateUtil.getCurrentTimeStamp(null);
        String dir = this.backupDir + "/backup/" + today;
        AppUtils.createFolderIfNotExist(dir);
        String filePath = dir + "/" + collectionName + ".json";
        // Perform export of collection data as needed
        // For example, you can iterate through documents and write them to a file
        // Here's just a simple example printing documents to the console
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            log.info("Exporting collection: " + collectionName);
            for (Document document : collection.find()) {
                fileWriter.write(document.toJson());
                fileWriter.write("\n"); // Add newline after each document
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
