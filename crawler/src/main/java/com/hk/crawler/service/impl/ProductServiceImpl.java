package com.hk.crawler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.dto.ProductRawDTO;
import com.hk.crawler.model.Product;
import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.Shop;
import com.hk.crawler.repository.IProductRawDataRepository;
import com.hk.crawler.repository.IProductRepository;
import com.hk.crawler.repository.IShopRepository;
import com.hk.crawler.service.IProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Override
    @Async("threadPoolTaskExecutor")
    @Transactional
    public void saveFromRawProduct() {
        log.info("Thread to save from Product raw data! " + Thread.currentThread().getName());
        List<ProductRawData> shopRawData = productRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                List<Product> products = new ArrayList<>();
                List<Product> participantJsonList = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<>(){});
                for (int j = 0; j < participantJsonList.size(); j++) {
                    Product dto = participantJsonList.get(j);
                    Product optionalProduct = productRepository.findByItemid(dto.getItemid());
                    if (optionalProduct == null) {
                        products.add(dto);
                    } else {
                        BeanUtils.copyProperties(dto, optionalProduct, "id"); // copy new value to old value
                        products.add(optionalProduct);
                    }
                }
                if (products.size() > 0) {
                    productRepository.saveAll(products);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error when parse data");
        }
    }

    @Override
    public List<Product> filterByShop(String shopid) {
        return productRepository.findAllByShopid(shopid);
    }
}
