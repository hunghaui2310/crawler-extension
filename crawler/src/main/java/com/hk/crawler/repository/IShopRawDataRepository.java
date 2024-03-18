package com.hk.crawler.repository;

import com.hk.crawler.model.ShopRawData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IShopRawDataRepository extends MongoRepository<ShopRawData, String> {

    @Query("{url:'?0'}")
    List<ShopRawData> findAllByUrl(String url);
}
