package com.hk.max.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.max.dto.ShopProductRawDTO;
import com.hk.max.model.ProductRawData;
import com.hk.max.model.ShopProductRawData;
import com.hk.max.model.ShopRawData;
import com.hk.max.repository.IProductRawDataRepository;
import com.hk.max.repository.IProductRepository;
import com.hk.max.repository.IShopProductRawDataRepository;
import com.hk.max.repository.IShopRawDataRepository;
import com.hk.max.service.IRawDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class RawDataServiceImpl implements IRawDataService {

    @Autowired
    private IShopProductRawDataRepository shopProductRawDataRepository;

    @Autowired
    private IProductRawDataRepository productRawDataRepository;

    @Autowired
    private IShopRawDataRepository shopRawDataRepository;

    @Override
    @Transactional
    public ShopProductRawData saveToShopProductRawData(ShopProductRawData shopProductRawData) {
        log.info("Saving data to Shop Product Raw Data! " + Thread.currentThread().getName());
        List<ShopProductRawData> productRawDataList = shopProductRawDataRepository.findAllByUrl(shopProductRawData.getUrl());
        ShopProductRawData newShop;
        if (productRawDataList.size() > 0) {
            newShop = productRawDataList.get(0);
            newShop.setData(shopProductRawData.getData());
        } else {
            newShop = new ShopProductRawData(shopProductRawData.getData(), shopProductRawData.getUrl());
        }
        log.info("Saved data to Shop Product Raw Data! " + Thread.currentThread().getName());
        return shopProductRawDataRepository.save(newShop);
    }

    @Override
    @Transactional
    public ShopRawData saveToShopRawData(ShopRawData shopRawData) {
        log.info("Saving data to Shop Raw Data! " + Thread.currentThread().getName());
        List<ShopRawData> productRawDataList = shopRawDataRepository.findAllByUrl(shopRawData.getUrl());
        ShopRawData newShop;
        if (productRawDataList.size() > 0) {
            newShop = productRawDataList.get(0);
            newShop.setData(shopRawData.getData());
        } else {
            newShop = new ShopRawData(shopRawData.getData(), shopRawData.getUrl());
        }
        log.info("Saved data to Shop Raw Data! " + Thread.currentThread().getName());
        return shopRawDataRepository.save(newShop);
    }

    @Override
    @Transactional
    public ProductRawData saveToProductRawData(ProductRawData productRawData) {
        log.info("Saving data to Product Raw Data! " + Thread.currentThread().getName());
        List<ProductRawData> productRawDataList = productRawDataRepository.findAllByUrl(productRawData.getUrl());
        ProductRawData newProduct;
        if (productRawDataList.size() > 0) {
            newProduct = productRawDataList.get(0);
            newProduct.setData(productRawData.getData());
        } else {
            newProduct = new ProductRawData(productRawData.getData(), productRawData.getUrl());
        }
        log.info("Saved data to Product Raw Data! " + Thread.currentThread().getName());
        return productRawDataRepository.save(newProduct);
    }

    @Override
    public Set<String> getDataCrawled() {
        List<ShopProductRawData> shopProductRawDataList = shopProductRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Set<String> catids = new HashSet<>();
            for (int i = 0; i < shopProductRawDataList.size(); i++) {
                String data = shopProductRawDataList.get(i).getData();
                if (data != null) {
                    List<ShopProductRawDTO> participantJsonList = mapper.readValue(data, new TypeReference<>(){});
                    for (int j = 0; j < participantJsonList.size(); j++) {
                        catids.add(participantJsonList.get(j).getCatid());
                    }
                }
            }
            return catids;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Error when parse data");
            return null;
        }
    }

    @Transactional
    @Override
    public void saveFromFile() {
        String filePath = "/Users/dabeeovina/Desktop/data_again.json";
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<ShopProductRawData> participantJsonList = mapper.readValue(new File(filePath), new TypeReference<>(){});
            for (ShopProductRawData productRawData : participantJsonList) {
                List<ShopProductRawData> shopProductRawDatas = shopProductRawDataRepository.findAllByUrl(productRawData.getUrl());
                if (shopProductRawDatas.size() > 0) {
                    for (ShopProductRawData shopProductRawData : shopProductRawDatas) {
                        shopProductRawData.setData(productRawData.getData());
                    }
                    shopProductRawDataRepository.saveAll(shopProductRawDatas);
                } else {
                    shopProductRawDataRepository.save(new ShopProductRawData(productRawData.getData(), productRawData.getUrl()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error when parse data");
        }
    }

    @Override
    public List<ProductRawData> getLastRawProductByShop(String shopid) {
        return productRawDataRepository.findAllByUrlRegex(shopid);
    }
}
