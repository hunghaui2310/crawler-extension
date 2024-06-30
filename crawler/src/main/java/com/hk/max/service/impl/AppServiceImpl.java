package com.hk.max.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hk.max.service.IAppService;
import com.hk.max.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AppServiceImpl implements IAppService {

    @Value("${spring.app.abs.dir}")
    private String parentDir;

    @Override
    public void resetCategory() throws IOException {
//        File currentDir = new File(System.getProperty("user.dir"));
//        String parentDir = currentDir.getParentFile().getAbsolutePath();
        String filePath = parentDir + "/extension/get_category_tree.json";

        File outputJsonFile = new File(parentDir + "/cates.json");
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file into a JsonNode
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        // Initialize the result list
        List<Map<String, Integer>> result = new ArrayList<>();
        // Extract catid values
        AppUtils.extractCatIds(rootNode.path("data").path("category_list"), result);
        // write to file
        objectMapper.writeValue(outputJsonFile, result);

    }

    @Override
    public void resetAccount() throws IOException {
//        File currentDir = new File(System.getProperty("user.dir"));
//        String parentDir = currentDir.getParentFile().getAbsolutePath();
        String filePath = parentDir + "/accounts.json";

        ObjectMapper objectMapper = new ObjectMapper();
        // Read JSON array from file
        ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(new File(filePath));

        // Update the status field for each object in the array
        for (JsonNode jsonNode : jsonArray) {
            if (jsonNode instanceof ObjectNode) {
                ((ObjectNode) jsonNode).put("status", 1);
            }
        }

        // Write the updated JSON array to the output file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), jsonArray);

    }
}
