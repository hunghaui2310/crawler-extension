package com.hk.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.model.Shop;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.repository.IShopRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopServiceImpl implements IShopService {

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IShopRawDataRepository shopRawDataRepository;

    @Override
    @Transactional
    public void saveFromRawShop(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Shop> participantJsonList = mapper.readValue(data, new TypeReference<List<Shop>>(){});
            shopRepository.saveAll(participantJsonList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error when parse data");
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    @SneakyThrows
    public void filterRawData() {
        List<ShopRawData> shopRawData = shopRawDataRepository.findAll();
        List<Shop> shops = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                List<Shop> participantJsonList = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<List<Shop>>(){});
                for (int j = 0; j < participantJsonList.size(); j++) {

                }
            }
            shopRepository.saveAll(shops);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error when parse data");
        }
    }
}
