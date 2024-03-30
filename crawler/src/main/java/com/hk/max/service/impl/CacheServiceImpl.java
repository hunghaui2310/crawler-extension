package com.hk.max.service.impl;

import com.hk.max.model.CacheModel;
import com.hk.max.repository.ICacheRepository;
import com.hk.max.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements ICacheService {

    @Autowired
    private ICacheRepository cacheRepository;

    @Override
    public CacheModel set(String key, String value) {
        CacheModel cacheModel = cacheRepository.findByKey(key);
        if (cacheModel != null) {
            cacheModel.setValue(value);
        }
        return cacheRepository.save(cacheModel == null ? new CacheModel(key, value) : cacheModel);
    }

    @Override
    public String get(String key) {
        CacheModel cacheModel = cacheRepository.findByKey(key);
        if (cacheModel != null) {
            return cacheModel.getValue();
        }
        return "0";
    }
}
