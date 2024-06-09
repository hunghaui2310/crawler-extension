package com.hk.max.repository;

import com.hk.max.model.Shop;
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

    List<Shop> findAllByCatid(String catid);

    Page<Shop> findAllByCatid(Pageable pageable, String catid);

    List<Shop> findAllByLastCrawlAtIsNotNull();

    //    Page<Shop> findAllByLastCrawlAtBeforeOrLastCrawlAtIsNull(Pageable pageable, Date lastCrawlAt);
    Page<Shop> findAllByLastCrawlAtIsNullAndCatid(Pageable pageable, String catid);

//    Page<Shop> findAllByLastCrawlAtAfterOrLastCrawlAtIsNull(Pageable pageable, Date lastCrawlAt);
//    Page<Shop> findAllByLastCrawlAtAfterOrLastCrawlAtIsNullAndCatid(Pageable pageable, Date lastCrawlAt, String catid);

//    List<Shop> findAllByLastCrawlAtBeforeOrLastCrawlAtIsNull(Date lastCrawlAt);
    List<Shop> findAllByLastCrawlAtIsNullAndCatid(String catid);

//    List<Shop> findAllByLastCrawlAtAfterOrLastCrawlAtIsNull(Date lastCrawlAt);
//    List<Shop> findAllByLastCrawlAtIsNullAndCatid(Date lastCrawlAt, String catid);

}
