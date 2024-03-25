package com.hk.max.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopExcelDTO {

    private String id;

    private String shopid;
    private String username;
    private String name;
    private String phoneNumber;
    private String address;
    private String totalRevenueMin;
    private String totalRevenueMax;
    private String shopLocation;
    private String shopCreateDate;
    private String shopUrl;

    public ShopExcelDTO(String shopid, String name, String address, String totalRevenueMin, String totalRevenueMax, String shopLocation) {
        this.shopid = shopid;
        this.name = name;
        this.address = address;
        this.totalRevenueMin = totalRevenueMin;
        this.totalRevenueMax = totalRevenueMax;
        this.shopLocation = shopLocation;
    }
}
