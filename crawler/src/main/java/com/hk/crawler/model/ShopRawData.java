package com.hk.crawler.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shop_raw_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopRawData {

    @Id
    private String _id;

    @NotNull(message = "Data cannot be null")
    private String data;

    @NotNull(message = "Data cannot be null")
    private String url;

    public ShopRawData(String data, String url) {
        this.data = data;
        this.url = url;
    }
}
