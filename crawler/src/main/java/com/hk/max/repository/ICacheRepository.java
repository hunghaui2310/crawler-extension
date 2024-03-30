package com.hk.max.repository;

import com.hk.max.model.CacheModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ICacheRepository extends MongoRepository<CacheModel, String> {

    @Query("{key:'?0'}")
    CacheModel findByKey(String key);
}
