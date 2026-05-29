package com.minipos.pos.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtil {
    private static ResourceBundle bundle;
    // Добавь эту переменную, если её нет:
    private static String currentLang = "ru";

    public static void setLocale(String lang) {
        currentLang = lang; // Сохраняем текущий язык
        bundle = ResourceBundle.getBundle("lang/lang", new Locale(lang));
    }

    // ВОТ ЭТОГО МЕТОДА У ТЕБЯ НЕТ, ПОЭТОМУ ОН КРАСНЫЙ
    public static String getCurrentLang() {
        return currentLang;
    }

    public static String get(String key) {
        if (bundle == null) setLocale("ru");
        return bundle.getString(key);
    }
}