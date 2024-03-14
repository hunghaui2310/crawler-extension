package com.hk.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.model.Shop;
import com.hk.crawler.repository.IShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopServiceImpl implements IShopService {

    @Autowired
    private IShopRepository shopRepository;

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
    public void filterRawData(String data) {

    }
}
