package com.hk.crawler.service;

import com.hk.crawler.model.ProductRawData;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;

public interface IRawDataService {

    ShopProductRawData saveToShopProductRawData(ShopProductRawData shopProductRawData);

    ShopRawData saveToShopRawData(ShopRawData shopRawData);

    ProductRawData saveToProductRawData(ProductRawData productRawData);
}
