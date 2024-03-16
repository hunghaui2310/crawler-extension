package com.hk.crawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "shop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class Shop {

    @Id
    private String id;

    @NotNull(message = "Shop Id cannot be null")
    @Indexed(unique = true)
    private String shopid;

    private String username;
    private String name;

    @Field("shop_location")
    private String shopLocation;
    private String description;
//    private String address;

    public Shop(String shopid, String name, String shopLocation) {
        this.shopid = shopid;
        this.name = name;
        this.shopLocation = shopLocation;
    }

    public Shop(String shopid, String shopLocation) {
        this.shopid = shopid;
        this.shopLocation = shopLocation;
    }
}
