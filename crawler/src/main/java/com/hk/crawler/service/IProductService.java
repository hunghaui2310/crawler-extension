package com.hk.crawler.service;

import com.hk.crawler.model.Product;

import java.util.List;

public interface IProductService {
    void saveFromRawProduct();

    List<Product> filterByShop(String shopid);

}
