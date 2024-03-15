package com.hk.crawler.repository;

import com.hk.crawler.model.ShopProductRawData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IShopProductRawDataRepository extends MongoRepository<ShopProductRawData, String> {
}
