package com.hk.crawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "shop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop {

    @Id
    @Field("_id")
    private ObjectId id;

    @NotNull(message = "Shop Id cannot be null")
    @Indexed(unique = true)
    private String shopid;

    private String username;
    @Field("shop_name")
    private String shopName;

    @Field("shop_location")
    private String shopLocation;
    private String description;
//    private String address;

    public Shop(String shopid, String shopName, String shopLocation) {
        this.shopid = shopid;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
    }

    public Shop(String shopid, String shopLocation) {
        this.shopid = shopid;
        this.shopLocation = shopLocation;
    }
}
