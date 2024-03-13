package com.hk.crawler.repository;

import com.hk.crawler.model.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IShopRepository extends MongoRepository<Shop, String> {

    @Query("{name:'?0'}")
    Shop findItemByShopId(String name);

    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    List<Shop> findAll(String category);

}
