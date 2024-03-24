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

//    Page<Shop> findAllByLastCrawlAtBeforeOrLastCrawlAtIsNull(Pageable pageable, Date lastCrawlAt);
    Page<Shop> findAllByCatidAndLastCrawlAtBeforeOrLastCrawlAtIsNull(Pageable pageable, String catid, Date lastCrawlAt);

//    Page<Shop> findAllByLastCrawlAtAfterOrLastCrawlAtIsNull(Pageable pageable, Date lastCrawlAt);
    Page<Shop> findAllByCatidAndLastCrawlAtAfterOrLastCrawlAtIsNull(Pageable pageable, String catid, Date lastCrawlAt);

//    List<Shop> findAllByLastCrawlAtBeforeOrLastCrawlAtIsNull(Date lastCrawlAt);
    List<Shop> findAllByCatidAndLastCrawlAtBeforeOrLastCrawlAtIsNull(String catid, Date lastCrawlAt);

//    List<Shop> findAllByLastCrawlAtAfterOrLastCrawlAtIsNull(Date lastCrawlAt);
    List<Shop> findAllByCatidAndLastCrawlAtAfterOrLastCrawlAtIsNull(String catid, Date lastCrawlAt);

}
