package com.hk.max.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.max.dto.ProductDTO;
import com.hk.max.model.Product;
import com.hk.max.model.ProductRawData;
import com.hk.max.repository.IProductRawDataRepository;
import com.hk.max.repository.IProductRepository;
import com.hk.max.service.IProductService;
import com.hk.max.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

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
        long totalRow = productRawDataRepository.countAllByLastRawAtBeforeOrLastRawAtIsNull(DateUtil.midnightFiftyDayAgo());
        int size = 100;
        int totalPages = (int) Math.ceil((double) totalRow / size);
        try {
            for (int page = 0; page < totalPages; page ++) {
                Pageable pageable = PageRequest.of(page, size);
                Page<ProductRawData> shopRawData = productRawDataRepository.findAllByLastRawAtBeforeOrLastRawAtIsNull(pageable, DateUtil.midnightFiftyDayAgo());
                ObjectMapper mapper = new ObjectMapper();
                List<ProductRawData> productRawData = shopRawData.getContent();
                for (int i = 0; i < productRawData.size(); i++) {
                    Set<Product> products = new HashSet<>();
                    productRawData.get(i).setLastRawAt(new Date());
                    String data = productRawData.get(i).getData();
                    if (data != null) {
                        List<Product> participantJsonList = mapper.readValue(data, new TypeReference<>() {});
                        for (int j = 0; j < participantJsonList.size(); j++) {
                            Product dto = participantJsonList.get(j);
                            Product optionalProduct = this.findByItemId(dto.getItemid());
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
                }
                productRawDataRepository.saveAll(productRawData);
            }
        log.info("Done to save from Product raw data! " + Thread.currentThread().getName());
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
