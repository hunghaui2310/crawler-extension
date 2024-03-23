package com.hk.max.utils;

public class DataUtil {

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        if (value instanceof String && value.equals("")) return defaultValue;
        return value == null ? defaultValue : value;
    }

    public static String buildShopUrl(String shopId) {
        return "https://shopee.vn/shop/" + shopId;
    }
}
