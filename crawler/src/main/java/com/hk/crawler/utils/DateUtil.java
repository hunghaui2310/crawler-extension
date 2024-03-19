package com.hk.crawler.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String getCurrentTimeStamp(String dateFormat) {
        if (dateFormat == null) {
            dateFormat = "dd_MM_yyyy";
        }
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    public static String convertTime(Long time) {
        if (time != null) {
            Instant instant = Instant.ofEpochSecond(time);

            // Convert Instant to LocalDateTime in UTC timezone
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));

            // Format LocalDateTime to string with specified format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dateTime.format(formatter);
        }
        return "";
    }
}
