package com.hk.crawler.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("shop")
@Getter()
@Setter()
public class Shop {

    @Id
    private String id;

    private String name;
    private int quantity;
    private String category;

}
