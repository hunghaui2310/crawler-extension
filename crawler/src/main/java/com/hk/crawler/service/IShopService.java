package com.hk.crawler.service;

import com.hk.crawler.dto.ShopExcelDTO;
import com.hk.crawler.dto.ShopRawDTO;
import com.hk.crawler.model.Shop;

import java.util.List;

public interface IShopService {

    void saveFromRawShop();

    void saveFromShopProductRawData();

    Shop updateShopInfo(ShopRawDTO shopRawDTO);

    List<String> getShopByPage(int page, int size);

    String getRevenueByShop(String shopid);

    List<ShopExcelDTO> getExcelData();
}
