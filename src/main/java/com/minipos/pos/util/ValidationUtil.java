package com.minipos.pos.util;

public class ValidationUtil {

    /**
     * Проверяет, что строка не пустая
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Проверяет, является ли строка числом (целым или дробным)
     */
    public static boolean isNumeric(String str) {
        if (str == null) return false;
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Проверяет, что цена или количество не отрицательные
     */
    public static boolean isPositive(double value) {
        return value >= 0;
    }

    /**
     * Комплексная проверка для товара
     */
    public static String validateProduct(String name, String priceStr, String stockStr) {
        if (isEmpty(name)) return "Название товара не может быть пустым!";

        if (!isNumeric(priceStr)) return "Цена должна быть числом!";
        if (Double.parseDouble(priceStr) <= 0) return "Цена должна быть больше нуля!";

        if (!isNumeric(stockStr)) return "Количество должно быть целым числом!";
        if (Integer.parseInt(stockStr) < 0) return "Количество не может быть отрицательным!";

        return null; // Ошибок нет
    }
}