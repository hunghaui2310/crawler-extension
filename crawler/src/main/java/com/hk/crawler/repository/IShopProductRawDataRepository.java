package com.hk.crawler.repository;

import com.hk.crawler.model.ShopProductRawData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IShopProductRawDataRepository extends MongoRepository<ShopProductRawData, String> {

    @Query("{url:'?0'}")
    List<ShopProductRawData> findAllByUrl(String url);
}
