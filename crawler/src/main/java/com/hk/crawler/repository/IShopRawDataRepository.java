package com.hk.crawler.repository;

import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IShopRawDataRepository extends MongoRepository<ShopRawData, String> {

}
