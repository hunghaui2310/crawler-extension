package com.hk.crawler.service;

import com.hk.crawler.dto.ShopExcelDTO;

import java.util.List;

public interface IShopService {

    void saveFromRawShop();

    void saveFromShopProductRawData();

    List<String> getShopByPage(int page, int size);

    String getRevenueByShop(String shopid);

    List<ShopExcelDTO> getExcelData();
}
