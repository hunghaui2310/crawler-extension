package com.hk.max.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static Date midnightFiftyDayAgo() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.DAY_OF_MONTH, -15);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }
}
