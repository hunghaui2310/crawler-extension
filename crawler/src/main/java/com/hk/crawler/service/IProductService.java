package com.hk.crawler.service;

import com.hk.crawler.dto.ProductDTO;
import com.hk.crawler.model.Product;

import java.util.List;

public interface IProductService {

    Product findByItemId(String itemid);

    void saveFromRawProduct();

    List<Product> filterByShop(String shopid);

    List<ProductDTO> getProductsByShop(String shopid);
}
