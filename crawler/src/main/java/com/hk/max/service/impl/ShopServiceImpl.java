package com.hk.max.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.max.concurrence.ShopProductRawTask;
import com.hk.max.dto.ShopDTO;
import com.hk.max.dto.ShopExcelDTO;
import com.hk.max.dto.ShopProductRawDTO;
import com.hk.max.dto.ShopRawDTO;
import com.hk.max.model.Product;
import com.hk.max.model.Shop;
import com.hk.max.model.ShopProductRawData;
import com.hk.max.model.ShopRawData;
import com.hk.max.repository.IShopProductRawDataRepository;
import com.hk.max.repository.IShopRawDataRepository;
import com.hk.max.repository.IShopRepository;
import com.hk.max.service.IProductService;
import com.hk.max.service.IShopService;
import com.hk.max.utils.CurrencyUtil;
import com.hk.max.utils.DataUtil;
import com.hk.max.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.*;

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
    @Transactional
    public Shop findByShopId(String shopid) {
        List<Shop> shops = shopRepository.findItemByShopId(shopid);
        if (shops.size() == 1) {
            return shops.get(0);
        }
        if (shops.size() > 1) {
            for (int i = 1; i < shops.size(); i ++) {
                shopRepository.delete(shops.get(i));
            }
            return shops.get(0);
        }
        return null;
    }

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
                Shop optionalShop = this.findByShopId(participantJson.getShopid());
                if (optionalShop == null) {
                    shops.add(participantJson);
                } else {
                    optionalShop.setShopName(participantJson.getShopName());
                    optionalShop.setDescription(participantJson.getDescription());
                    optionalShop.setUsername(participantJson.getUsername());
//                    BeanUtils.copyProperties(participantJson, optionalShop, "id", "shopLocation", "catid", "ctime"); // copy new value to old value
                    shops.add(optionalShop);
                }
                if (shops.size() > 0) {
                    shopRepository.saveAll(shops);
                }
            }
        } catch (Exception e) {
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
                        Shop optionalShop = this.findByShopId(dto.getShopid());
                        if (optionalShop == null) {
                            Shop shop = new Shop(dto.getShopid(), dto.getShopLocation(), dto.getCatid());
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
        if (shopRawDTO.getShopid() == null || shopRawDTO.getShopid().equals("")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request. shopid cannot be null");
        }
        Shop newShopData = this.findByShopId(shopRawDTO.getShopid());
        if (newShopData == null) {
            return null;
        }
        // update shop info
        newShopData.setRawInfo(shopRawDTO.getRawInfo());
        newShopData.setDetailPhone(shopRawDTO.getDetailPhone());
        newShopData.setDetailAddress(shopRawDTO.getDetailAddress());
        if (shopRawDTO.getCtime() != null) {
            newShopData.setCtime(shopRawDTO.getCtime());
        }
        shopRepository.save(newShopData);
        return newShopData;
    }

    @Override
    public List<String> getShopByPage(boolean isCrawled, String catid, int page, int size) {
        log.info("Thread to filter shop by id! " + Thread.currentThread().getName());
        List<String> ids = new ArrayList<>();
        List<Shop> listShop;
        if (page != 0) {
            Pageable pageable = PageRequest.of(page, size);
            Page shopPage;
            if (isCrawled) {
                shopPage = shopRepository.findAllByCatid(pageable, catid);
            } else {
                shopPage = shopRepository.findAllByLastCrawlAtIsNullAndCatid(pageable, catid);
            }
            listShop = shopPage.getContent();
        } else {
            if (isCrawled) {
                listShop = shopRepository.findAllByLastCrawlAtIsNullAndCatid(catid);
            } else {
                listShop = shopRepository.findAllByLastCrawlAtIsNullAndCatid(catid);
            }
        }
        for (Shop shop : listShop) {
            ids.add(shop.getShopid());
        }
        return ids;
    }

    @Override
    public ShopDTO getRevenueByShop(String shopid, boolean isMin) {
        List<Product> products = productService.filterByShop(shopid);
        Long totalRevenue = 0L;
        for (Product product : products) {
            Long price = isMin ? product.getPrice_min() : product.getPrice_max();
            if (price == null) {
                price = product.getPrice();
            }
            totalRevenue += price * product.getHistorical_sold();

        }
        return new ShopDTO(CurrencyUtil.removeLastNDigits(totalRevenue, 5), products.size());
//        return CurrencyUtil.toMoneyVND(totalRevenue);
    }

    @Override
    public List<ShopExcelDTO> getExcelData(String catid) {
        List<ShopExcelDTO> shopExcelDTOS = new ArrayList<>();
        List<Shop> shops = shopRepository.findAllByCatid(catid);
        for (Shop shop : shops) {
            ShopDTO shopDTO = this.getRevenueByShop(shop.getShopid(), true);
            Long totalRevenueMin = shopDTO.getTotalRevenue();
            Long totalRevenueMax = this.getRevenueByShop(shop.getShopid(), false).getTotalRevenue();
            ShopExcelDTO shopExcelDTO = new ShopExcelDTO(shop.getShopid(), shop.getShopName(), shop.getDetailAddress(), totalRevenueMin, totalRevenueMax, shop.getShopLocation());
            shopExcelDTO.setUsername(shop.getUsername());
            shopExcelDTO.setPhoneNumber(shop.getDetailPhone());
            shopExcelDTO.setShopCreateDate(DateUtil.convertTime(shop.getCtime()));
            shopExcelDTO.setShopUrl(DataUtil.buildShopUrl(shop.getShopid()));
            shopExcelDTO.setTotalProduct(shopDTO.getTotalProduct());
            shopExcelDTO.setIsActive(shop.getLastCrawlAt() != null || shop.getIsActive() == 1);
            shopExcelDTOS.add(shopExcelDTO);
        }
        return shopExcelDTOS;
    }

    @Override
    public void updateShopLastCrawlAt(String shopid) {
        Shop shop = this.findByShopId(shopid);
        shop.setLastCrawlAt(Instant.now());
        shop.setIsActive(1);
        shopRepository.save(shop);
    }

    @Override
    public void updateShopIsNotActive(String shopid) {
        Shop shop = this.findByShopId(shopid);
        shop.setIsActive(0);
        shopRepository.save(shop);
    }

    @Override
    public void testThread() {
        for (int i = 0; i < 10; i++) {
            ShopProductRawTask shopProductRawTask = new ShopProductRawTask();
            Thread thread = new Thread(shopProductRawTask);
            thread.start();
        }
    }
}
