package com.hk.max.repository;

import com.hk.max.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICategoryRepository extends MongoRepository<Category, String> {
}
