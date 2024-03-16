package com.hk.crawler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getCurrentTimeStamp(String dateFormat) {
        if (dateFormat == null) {
            dateFormat = "dd/MM/yyyy";
        }
        return new SimpleDateFormat(dateFormat).format(new Date());
    }
}
