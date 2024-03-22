package com.hk.max.repository;

import com.hk.max.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IProductRepository extends MongoRepository<Product, String> {

    @Query("{shopid:'?0'}")
    List<Product> findAllByShopid(String shopid);

    @Query("{itemid:'?0'}")
    List<Product> findAllByItemid(String itemid);
}
