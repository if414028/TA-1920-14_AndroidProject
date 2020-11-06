package com.ditenun.appditenun.function.util;

import java.text.NumberFormat;
import java.util.Locale;

public class TextUtil {
    private static final TextUtil ourInstance = new TextUtil();

    public static TextUtil getInstance() {
        return ourInstance;
    }

    private TextUtil() {
    }

    public String formatToRp(Double nominal) {
        if (nominal == null) return "";
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(nominal).replaceAll("Rp", "Rp ");
    }
}
