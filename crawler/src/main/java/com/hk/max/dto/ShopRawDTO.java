package com.hk.max.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopRawDTO {

    private String shopid;
    private String username;
    private String shopName;
    private String description;
    private Long ctime;

    private String rawInfo;
    private String detailAddress;
    private String detailPhone;

    public static class ShopExcelDTO {
    }
}
