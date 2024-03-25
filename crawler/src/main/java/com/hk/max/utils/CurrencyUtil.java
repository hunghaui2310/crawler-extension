package com.hk.max.utils;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtil {

    public static String toMoneyVND(Long price) {
        Locale locale = new Locale("vi", "VN");
        Currency currency = Currency.getInstance("VND");

        DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
        df.setCurrency(currency);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setCurrency(currency);
        String currencySymbol = Currency.getInstance(locale).getSymbol();
        return numberFormat.format(price).replace(currencySymbol, "").trim();
    }

    public static Long removeLastNDigits(Long x, long n) {
        return (long) (x / Math.pow(10, n));
    }
}
