package com.hk.max.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "product")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @Id
    @Field("_id")
    private ObjectId id;

    @NotNull(message = "Item Id cannot be null")
    private String itemid;
    @NotNull(message = "Shop Id cannot be null")
    private String shopid;
    private String name;
//    private String currency;
    private Integer stock;
    private Integer status;
    private Integer sold;
    private Integer historical_sold;
//    private Boolean liked;
//    private Integer liked_count;
    private Integer view_count;
    @NotNull(message = "Category Id cannot be null")
    private String catid;
//    private String brand;
//    private Integer cmt_count;
//    private Integer flag;
//    private Integer cb_option;
    private String item_status;
//    @NotNull(message = "Price cannot be null")
    private Long price;
    private Long price_min;
    private Long price_max;
//    private Long price_min_before_discount;
//    private Long price_max_before_discount;
//    private Long hidden_price_display;
//    private Long price_before_discount;
//    private Boolean has_lowest_price_guarantee;
//    private Integer show_discount;
//    private Integer raw_discount;
    private String discount;
//    private Boolean is_category_failed;
//    private Integer item_type;
//    private String reference_item_id;
//    private Boolean is_adult;
//    private Boolean shopee_verified;
//    private Boolean is_official_shop;
//    private Boolean show_official_shop_label;
//    private Boolean show_shopee_verified_label;
//    private Boolean show_official_shop_label_in_title;
//    private Boolean is_cc_installment_payment_eligible;
//    private Boolean is_non_cc_installment_payment_eligible;
//    private Boolean show_free_shipping;
//    private String preview_info;
//    private String exclusive_price_info;
//    private Object add_on_deal_info;
//    private Boolean is_preferred_plus_seller;
    @NotNull(message = "Shop Location cannot be null")
    private String shop_location;
//    private String voucher_info;
//    private Boolean can_use_cod;
//    private Boolean is_on_flash_sale;
//    private Boolean is_live_streaming_price;
//    private Boolean is_mart;
//    private String free_shipping_info;
    private String model_id;

    @CreatedDate
    @Field("created_date")
    private Date createdDate;

    @Override
    public int hashCode() {
        return itemid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product))
            return false;

        Product mdc = (Product) obj;
        return mdc.itemid.equals(itemid);
    }
}
