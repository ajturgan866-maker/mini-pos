package com.minipos.pos.controller.cashier;

import com.minipos.pos.model.Product;
import com.minipos.pos.util.I18nUtil;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CartController {

    private static final Map<Product, Integer> currentCart = new HashMap<>();

    public static void addProduct(Product product) {
        currentCart.put(product, currentCart.getOrDefault(product, 0) + 1);
        // Используем I18n для логирования
        System.out.println(String.format(I18nUtil.get("cart.added"), product.getName(), currentCart.get(product)));
    }

    public static void removeProduct(Product product) {
        if (!currentCart.containsKey(product)) return;

        int count = currentCart.get(product);
        if (count > 1) {
            currentCart.put(product, count - 1);
        } else {
            currentCart.remove(product);
        }
    }

    public static BigDecimal getTotalSum() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : currentCart.entrySet()) {
            total = total.add(entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue())));
        }
        return total;
    }

    public static void clearCart() {
        currentCart.clear();
    }

    // Возвращаем неизменяемую копию для защиты данных
    public static Map<Product, Integer> getCurrentCart() {
        return Collections.unmodifiableMap(currentCart);
    }
}