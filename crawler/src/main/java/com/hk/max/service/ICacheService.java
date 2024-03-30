package com.hk.max.service;

import com.hk.max.model.CacheModel;

public interface ICacheService {

    CacheModel set(String key, String value);

    String get(String key);
}
