package com.hk.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.dto.ShopRawDto;
import com.hk.crawler.model.Shop;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.repository.IShopRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ShopServiceImpl implements IShopService {

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IShopRawDataRepository shopRawDataRepository;

    @Override
    @Async("threadPoolTaskExecutor")
    @SneakyThrows
    @Transactional
    public void saveFromRawShop() {
        log.info("Thread to save shop from raw data! " + Thread.currentThread().getName());
        List<ShopRawData> shopRawData = shopRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                List<Shop> shops = new ArrayList<>();
                List<ShopRawDto> participantJsonList = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<>(){});
                for (int j = 0; j < participantJsonList.size(); j++) {
                    ShopRawDto dto = participantJsonList.get(j);
                    Shop optionalShop = shopRepository.findItemByShopId(dto.getShopid());
                    if (optionalShop == null) {
                        Shop shop = new Shop(dto.getShopid(), dto.getShop_location());
                        shops.add(shop);
                    }
                }
                shopRepository.saveAll(shops);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error when parse data");
        }
    }

    @Override
//    @Async("threadPoolTaskExecutor")
//    @SneakyThrows
    @Transactional
    public List<String> getShopByPage(int page, int size) {
        log.info("Thread to filter shop by id! " + Thread.currentThread().getName());
        List<String> ids = new ArrayList<>();
        List<Shop> listShop;
        if (page != 0) {
            Pageable pageable = PageRequest.of(page, size);
            Page shopPage = shopRepository.findAll(pageable);
            listShop = shopPage.getContent();
        } else {
            listShop = shopRepository.findAll();
        }
        for (Shop shop : listShop) {
            ids.add(shop.getShopid());
        }
        return ids;
    }
}
