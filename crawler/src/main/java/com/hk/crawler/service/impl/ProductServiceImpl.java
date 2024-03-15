package com.hk.crawler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.dto.ProductRawDto;
import com.hk.crawler.model.Product;
import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.Shop;
import com.hk.crawler.repository.IProductRawDataRepository;
import com.hk.crawler.repository.IProductRepository;
import com.hk.crawler.repository.IShopRepository;
import com.hk.crawler.service.IProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRawDataRepository productRawDataRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IShopRepository shopRepository;

    @Override
    @Async("threadPoolTaskExecutor")
    @SneakyThrows
    @Transactional
    public void saveFromRawProduct() {
        log.info("Thread to save Product from raw data! " + Thread.currentThread().getName());
        List<ProductRawData> shopRawData = productRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                List<Shop> shops = new ArrayList<>();
                List<ProductRawDto> participantJsonList = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<>(){});
                for (int j = 0; j < participantJsonList.size(); j++) {
                    ProductRawDto dto = participantJsonList.get(j);
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
}
