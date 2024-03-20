package com.hk.crawler.repository;

import com.hk.crawler.model.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface IShopRepository extends MongoRepository<Shop, String> {

    @Query("{shopid:'?0'}")
    List<Shop> findItemByShopId(String shopId);

    Page<Shop> findAll(Pageable pageable);

    Page<Shop> findAllByLastCrawlAtBefore(Pageable pageable, Date lastCrawlAt);
    Page<Shop> findAllByLastCrawlAtAfter(Pageable pageable, Date lastCrawlAt);

    List<Shop> findAllByLastCrawlAtBefore(Date lastCrawlAt);
    List<Shop> findAllByLastCrawlAtAfter(Date lastCrawlAt);
}
