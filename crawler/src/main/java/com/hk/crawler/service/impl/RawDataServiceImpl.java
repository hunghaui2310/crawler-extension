package com.hk.crawler.service.impl;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IProductRawDataRepository;
import com.hk.crawler.repository.IShopProductRawDataRepository;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.service.IRawDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Async("threadPoolTaskExecutor")
    @Transactional
    public ShopProductRawData saveToShopProductRawData(ShopProductRawData shopProductRawData) {
        log.info("Saving data to Shop Product Raw Data! " + Thread.currentThread().getName());
        List<ShopProductRawData> productRawDataList = shopProductRawDataRepository.findAllByUrl(shopProductRawData.getUrl());
        ShopProductRawData newShop;
        if (productRawDataList.size() > 0) {
            newShop = productRawDataList.get(0);
            newShop.setData(shopProductRawData.getData());
        } else {
            newShop = new ShopProductRawData(shopProductRawData.getData(), shopProductRawData.getUrl(), shopProductRawData.getCatid());
        }
        log.info("Saved data to Shop Product Raw Data! " + Thread.currentThread().getName());
        return shopProductRawDataRepository.save(newShop);
    }

    @Override
    @Async("threadPoolTaskExecutor")
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
    @Async("threadPoolTaskExecutor")
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
    public List<String> getDataCrawled() {
        List<ShopProductRawData> shopProductRawDataList = shopProductRawDataRepository.findAll();
        return shopProductRawDataList.stream().map(ShopProductRawData::getCatid).collect(Collectors.toList());
    }
}
