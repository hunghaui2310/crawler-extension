package com.hk.crawler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.crawler.dto.ProductRawDTO;
import com.hk.crawler.dto.ShopExcelDTO;
import com.hk.crawler.dto.ShopProductRawDTO;
import com.hk.crawler.dto.ShopRawDTO;
import com.hk.crawler.model.Product;
import com.hk.crawler.model.Shop;
import com.hk.crawler.model.ShopProductRawData;
import com.hk.crawler.model.ShopRawData;
import com.hk.crawler.repository.IShopProductRawDataRepository;
import com.hk.crawler.repository.IShopRawDataRepository;
import com.hk.crawler.repository.IShopRepository;
import com.hk.crawler.service.IProductService;
import com.hk.crawler.service.IShopService;
import com.hk.crawler.utils.CurrencyUtil;
import com.hk.crawler.utils.DataUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ShopServiceImpl implements IShopService {

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private IShopRawDataRepository shopRawDataRepository;

    @Autowired
    private IShopProductRawDataRepository shopProductRawDataRepository;

    @Override
    @Async("threadPoolTaskExecutor")
    @Transactional
    public void saveFromRawShop() {
        log.info("Thread to save shop from shop raw data! " + Thread.currentThread().getName());
        List<ShopRawData> shopRawData = shopRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                Set<Shop> shops = new HashSet<>();
                Shop participantJson = mapper.readValue(shopRawData.get(i).getData(), new TypeReference<>(){});
                List<Shop> optionalShop = shopRepository.findItemByShopId(participantJson.getShopid());
                if (optionalShop.size() == 0) {
                    shops.add(participantJson);
                } else {
                    BeanUtils.copyProperties(participantJson, optionalShop.get(0), "id", "shopLocation"); // copy new value to old value
                    shops.add(optionalShop.get(0));
                }
                if (shops.size() > 0) {
                    shopRepository.saveAll(shops);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error when parse data");
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    @Transactional
    public void saveFromShopProductRawData() {
        log.info("Thread to save shop from shop-product raw data! " + Thread.currentThread().getName());
        List<ShopProductRawData> shopRawData = shopProductRawDataRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < shopRawData.size(); i++) {
                Set<Shop> shops = new HashSet<>();
                String data = shopRawData.get(i).getData();
                if (data != null) {
                    List<ShopProductRawDTO> participantJsonList = mapper.readValue(data, new TypeReference<>(){});
                    for (int j = 0; j < participantJsonList.size(); j++) {
                        ShopProductRawDTO dto = participantJsonList.get(j);
                        List<Shop> optionalShop = shopRepository.findItemByShopId(dto.getShopid());
                        if (optionalShop.size() == 0) {
                            Shop shop = new Shop(dto.getShopid(), dto.getShopLocation()
                            );
                            shops.add(shop);
                        }
                    }
                    shopRepository.saveAll(shops);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Error when parse data");
        }
    }

    @Override
    @Transactional
    public Shop updateShopInfo(ShopRawDTO shopRawDTO) {
        List<Shop> shops = shopRepository.findItemByShopId(shopRawDTO.getShopid());
        if (shops.size() == 0) {
            return null;
        }
        // if shop has size > 1, that mean shop was be duplicated -> delete shop duplicate
        if (shops.size() > 1) {
            for (int i = 1; i < shops.size(); i ++) {
                shopRepository.delete(shops.get(i));
            }
        }
        // update shop info
        Shop newShopData = shops.get(0);
        newShopData.setRawInfo(shopRawDTO.getRawInfo());
        newShopData.setDetailPhone(shopRawDTO.getDetailPhone());
        newShopData.setDetailAddress(shopRawDTO.getDetailAddress());
        shopRepository.save(newShopData);
        return newShopData;
    }

    @Override
    public List<String> getShopByPage(int page, int size) {
        log.info("Thread to filter shop by id! " + Thread.currentThread().getName());
        List<String> ids = new ArrayList<>();
        List<Shop> listShop;
        if (page != 0) {
            Pageable pageable = PageRequest.of(page, size);
            Page shopPage = shopRepository.findAll(pageable);
            listShop = shopPage.getContent();
        } else {
            listShop = shopRepository.findAll();
        }
        for (Shop shop : listShop) {
            ids.add(shop.getShopid());
        }
        return ids;
    }

    @Override
    public String getRevenueByShop(String shopid) {
        List<Product> products = productService.filterByShop(shopid);
        Long totalRevenue = 0L;
        for (Product product : products) {
            Long price = product.getPrice_max();
            if (price == null) {
                price = product.getPrice();
            }
            totalRevenue += price * product.getHistorical_sold();

        }
        totalRevenue = CurrencyUtil.removeLastNDigits(totalRevenue, 5);
        return CurrencyUtil.toMoneyVND(totalRevenue);
    }

    @Override
    public List<ShopExcelDTO> getExcelData() {
        List<ShopExcelDTO> shopExcelDTOS = new ArrayList<>();
        List<Shop> shops = shopRepository.findAll();
        for (Shop shop : shops) {
            String totalRevenue = this.getRevenueByShop(shop.getShopid());
            ShopExcelDTO shopExcelDTO = new ShopExcelDTO(shop.getShopid(), shop.getShopName(), shop.getAddress(), totalRevenue, shop.getShopLocation());
            shopExcelDTO.setUsername(shop.getUsername());
            shopExcelDTOS.add(shopExcelDTO);
        }
        return shopExcelDTOS;
    }
}
