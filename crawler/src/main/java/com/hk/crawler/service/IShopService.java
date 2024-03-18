package com.hk.crawler.service;

import java.util.List;

public interface IShopService {

    void saveFromRawShop();

    void saveFromShopProductRawData();

    List<String> getShopByPage(int page, int size);
}
