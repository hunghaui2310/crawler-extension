package com.hk.crawler.dto;

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

    public static class ShopExcelDTO {
    }
}
