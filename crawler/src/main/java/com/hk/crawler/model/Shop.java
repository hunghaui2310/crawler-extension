package com.hk.crawler.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "shop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    @Id
    private String id;

    @NotNull(message = "Shop Id cannot be null")
    private String shopid;

    private String name;

    @Field("shop_location")
    private String shopLocation;
//    private String phoneNumber;
//    private String address;

    public Shop(String shopid, String name, String shopLocation) {
        this.shopid = shopid;
        this.name = name;
        this.shopLocation = shopLocation;
    }
}
