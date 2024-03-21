package com.hk.crawler.service;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;

import java.util.Set;

public interface IRawDataService {

    ShopProductRawData saveToShopProductRawData(ShopProductRawData shopProductRawData);

    ShopRawData saveToShopRawData(ShopRawData shopRawData);

    ProductRawData saveToProductRawData(ProductRawData productRawData);

    Set<String> getDataCrawled();
}
