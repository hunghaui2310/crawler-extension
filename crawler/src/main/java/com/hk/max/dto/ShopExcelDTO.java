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
    private Long totalRevenueMin;
    private Long totalRevenueMax;
    private String shopLocation;
    private String shopCreateDate;
    private String shopUrl;
    private Long totalProduct;
    private Boolean isActive;


    public ShopExcelDTO(String shopid, String name, String address, Long totalRevenueMin, Long totalRevenueMax, String shopLocation) {
        this.shopid = shopid;
        this.name = name;
        this.address = address;
        this.totalRevenueMin = totalRevenueMin;
        this.totalRevenueMax = totalRevenueMax;
        this.shopLocation = shopLocation;
    }

    public String getIsActiveStr() {
        return this.isActive ? "Hoạt động" : "Không hoạt động";
    }
}
