package com.hk.max.service;

import com.hk.max.dto.ProductDTO;
import com.hk.max.model.Product;

import java.util.List;

public interface IProductService {

    Product findByItemId(String itemid);

    void saveFromRawProduct();

    List<Product> filterByShop(String shopid);

    List<ProductDTO> getProductsByShop(String shopid);
}
