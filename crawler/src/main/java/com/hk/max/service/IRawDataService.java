package com.hk.max.service;

import com.hk.max.model.ProductRawData;
import com.hk.max.model.ShopProductRawData;
import com.hk.max.model.ShopRawData;

import java.util.Set;

public interface IRawDataService {

    ShopProductRawData saveToShopProductRawData(ShopProductRawData shopProductRawData);

    ShopRawData saveToShopRawData(ShopRawData shopRawData);

    ProductRawData saveToProductRawData(ProductRawData productRawData);

    Set<String> getDataCrawled();
}
