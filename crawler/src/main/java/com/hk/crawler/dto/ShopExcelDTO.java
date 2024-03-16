package com.hk.crawler.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopExcelDTO {

    private String id;

    private String shopId;

    private String name;
    private String phoneNumber;
    private String address;
    private Integer STT;

    public ShopExcelDTO(Integer STT, String name, String phoneNumber, String address) {
        this.STT = STT;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
