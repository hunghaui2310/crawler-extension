package com.hk.max.service;

import com.hk.max.dto.ShopExcelDTO;
import com.hk.max.dto.ShopRawDTO;
import com.hk.max.model.Shop;

import java.util.List;

public interface IShopService {

    Shop findByShopId(String shopid);

    void saveFromRawShop();

    void saveFromShopProductRawData();

    Shop updateShopInfo(ShopRawDTO shopRawDTO);

    List<String> getShopByPage(boolean isCrawled, String catid, int page, int size);

    Long getRevenueByShop(String shopid, boolean isMin);

    List<ShopExcelDTO> getExcelData(String catid);

    void updateShopLastCrawlAt(String shopid);

    void testThread();
}
