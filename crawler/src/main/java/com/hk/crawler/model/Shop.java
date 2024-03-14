package com.hk.crawler.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    @Id
    private String id;

    @NotNull(message = "Shop Id cannot be null")
    private String shopId;

    private String name;
    private int quantity;
    private Boolean isCheck;

    public Shop(String name, int quantity, Boolean isCheck) {
        this.name = name;
        this.quantity = quantity;
        this.isCheck = isCheck;
    }

}
