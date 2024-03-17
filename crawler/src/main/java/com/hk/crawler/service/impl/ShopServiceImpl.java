package com.hk.crawler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.dto.ProductRawDTO;
import com.hk.crawler.dto.ShopProductRawDTO;
import com.hk.crawler.dto.ShopRawDTO;
import com.hk.crawler.model.Shop;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopProductRawDataRepository;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.repository.IShopRepository;
import com.hk.crawler.service.IShopService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShopServiceImpl implements IShopService {

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IShopRawDataRepository shopRawDataRepository;

    @Autowired
    private IShopProductRawDataRepository shopProductRawDataRepository;

    @Override
    @Async("threadPoolTaskExecutor")
    @SneakyThrows
    @Transactional
    public void saveFromRawShop() {
        log.info("Thread to save shop from shop raw data! " + Thread.currentThread().getName());
        List<ShopRawData> shopRawData = shopRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                List<Shop> shops = new ArrayList<>();
                List<Shop> participantJsonList = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<>(){});
                for (int j = 0; j < participantJsonList.size(); j++) {
                    Shop dto = participantJsonList.get(j);
                    Shop optionalShop = shopRepository.findItemByShopId(dto.getShopid());
                    if (optionalShop == null) {
                        shops.add(dto);
                    } else {
                        BeanUtils.copyProperties(dto, optionalShop, "id"); // copy new value to old value
                        shops.add(optionalShop);
                    }
                }
                if (shops.size() > 0) {
                    shopRepository.saveAll(shops);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error when parse data");
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    @SneakyThrows
    @Transactional
    public void saveFromShopProductRawData() {
        log.info("Thread to save shop from shop-product raw data! " + Thread.currentThread().getName());
        List<ShopProductRawData> shopRawData = shopProductRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                List<Shop> shops = new ArrayList<>();
                List<ShopProductRawDTO> participantJsonList = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<>(){});
                for (int j = 0; j < participantJsonList.size(); j++) {
                    ShopProductRawDTO dto = participantJsonList.get(j);
                    Shop optionalShop = shopRepository.findItemByShopId(dto.getShopid());
                    if (optionalShop == null) {
                        Shop shop = new Shop(dto.getShopid(), dto.getShopLocation()
                        );
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
