package com.hk.crawler.service;

import java.util.List;

public interface IShopService {

    void saveFromRawShop();

    List<String> getShopByPage(int page, int size);
}
