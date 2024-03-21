package com.hk.crawler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

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
    private Long ctime;
    @Field("raw_info")
    private String rawInfo;
    @Field("detail_address")
    private String detailAddress;
    @Field("detail_phone")
    private String detailPhone;

    @Field("last_crawl_at")
    private Instant lastCrawlAt;

//    public void setRawInfo(String rawInfo) {
//        if (rawInfo == null || "".equals(rawInfo)) return;
//        if (this.rawInfo == null || this.getRawInfo().equals("")) {
//            this.rawInfo = rawInfo;
//        } else {
//            this.rawInfo = this.getRawInfo().concat("\n").concat(rawInfo);
//        }
//    }

    public void setDetailAddress(String detailAddress) {
        if (detailAddress == null || "".equals(detailAddress)) return;
        if (this.detailAddress == null || this.getDetailAddress().equals("")) {
            this.detailAddress = detailAddress;
        } else {
            this.detailAddress = this.getDetailAddress().concat("\n").concat(detailAddress);
        }
    }

    public void setDetailPhone(String detailPhone) {
        if (detailPhone == null || "".equals(detailPhone)) return;
        if (this.detailPhone == null || this.getDetailPhone().equals("")) {
            this.detailPhone = detailPhone;
        } else {
            this.detailPhone = this.getDetailPhone().concat("\n").concat(detailPhone);
        }
    }

    @CreatedDate
    @Field("created_date")
    private Date createdDate;

    public Shop(String shopid, String shopName, String shopLocation) {
        this.shopid = shopid;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
    }

    public Shop(String shopid, String shopLocation) {
        this.shopid = shopid;
        this.shopLocation = shopLocation;
    }

    @Override
    public int hashCode() {
        return shopid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Shop))
            return false;

        Shop mdc = (Shop) obj;
        return mdc.shopid.equals(shopid);
    }
}
