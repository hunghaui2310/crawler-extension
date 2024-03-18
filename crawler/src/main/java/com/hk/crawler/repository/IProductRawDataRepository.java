package com.hk.crawler.repository;

import com.hk.crawler.model.ProductRawData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IProductRawDataRepository extends MongoRepository<ProductRawData, String> {

    @Query("{url:'?0'}")
    List<ProductRawData> findAllByUrl(String url);
}
