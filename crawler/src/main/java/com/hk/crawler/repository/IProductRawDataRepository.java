package com.hk.crawler.repository;

import com.hk.crawler.model.ProductRawData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductRawDataRepository extends MongoRepository<ProductRawData, String> {
}
