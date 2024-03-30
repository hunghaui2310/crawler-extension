package com.hk.max.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "cache")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CacheModel {

    @Id
    @Field("_id")
    private ObjectId id;

    private String key;
    private String value;

    public CacheModel(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
