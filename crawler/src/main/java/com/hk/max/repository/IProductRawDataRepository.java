package com.hk.max.repository;

import com.hk.max.model.ProductRawData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface IProductRawDataRepository extends MongoRepository<ProductRawData, String> {

    @Query("{url:'?0'}")
    List<ProductRawData> findAllByUrl(String url);

    @Query(value = "{'url': {$regex : ?0, $options: 'i'}}")
    List<ProductRawData> findAllByUrlRegex(String url);

    Page<ProductRawData> findAllByLastRawAtBeforeOrLastRawAtIsNull(Pageable pageable, Date afterAt);

    long countAllByLastRawAtBeforeOrLastRawAtIsNull(Date afterAt);
}
