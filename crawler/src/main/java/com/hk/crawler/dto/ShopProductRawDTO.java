package com.hk.crawler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopProductRawDTO {
    private String shopid;
    private String shopLocation;
    private String catid;
}
