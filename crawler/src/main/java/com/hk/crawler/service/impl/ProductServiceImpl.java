package com.hk.crawler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.dto.ProductDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRawDataRepository productRawDataRepository;

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Product findByItemId(String itemid) {
        List<Product> products = productRepository.findAllByItemid(itemid);
        if (products.size() == 1) {
            return products.get(0);
        }
        if (products.size() > 1) {
            for (int i = 1; i < products.size(); i ++) {
                productRepository.delete(products.get(i));
            }
            return products.get(0);
        }
        return null;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    @Transactional
    public void saveFromRawProduct() {
        log.info("Thread to save from Product raw data! " + Thread.currentThread().getName());
        List<ProductRawData> shopRawData = productRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                Set<Product> products = new HashSet<>();
                String data = shopRawData.get(i).getData();
                if (data != null) {
                    List<Product> participantJsonList = mapper.readValue(data, new TypeReference<>(){});
                    for (int j = 0; j < participantJsonList.size(); j++) {
                        Product dto = participantJsonList.get(j);
                        List<Product> optionalProduct = productRepository.findAllByShopid(dto.getItemid());
                        if (optionalProduct.size() == 0) {
                            products.add(dto);
                        } else {
                            BeanUtils.copyProperties(dto, optionalProduct.get(0), "id"); // copy new value to old value
                            products.add(optionalProduct.get(0));
                        }
                    }
                    if (products.size() > 0) {
                        productRepository.saveAll(products);
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Error when parse data");
        }
    }

    @Override
    public List<Product> filterByShop(String shopid) {
        return productRepository.findAllByShopid(shopid);
    }

    @Override
    public List<ProductDTO> getProductsByShop(String shopid) {
        List<Product> products = this.filterByShop(shopid);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(new ProductDTO(product.getItemid(), product.getShopid()));
        }
        return productDTOS;
    }
}
