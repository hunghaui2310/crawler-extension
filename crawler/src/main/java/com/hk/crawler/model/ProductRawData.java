package com.hk.crawler.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "product_raw_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRawData {

    @Id
    private String _id;

    @NotNull(message = "Data cannot be null")
    private String data;

    private String url;

    @CreatedDate
    @Field("created_date")
    private Date createdDate;

    public ProductRawData(String data, String url) {
        this.data = data;
        this.url = url;
        this.createdDate = new Date();
    }
}
