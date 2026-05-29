package com.minipos.pos.service;

import com.minipos.pos.controller.cashier.CartController;
import com.minipos.pos.model.Order;
import com.minipos.pos.model.Product;
import com.minipos.pos.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository = new OrderRepository();

    /**
     * --- ЛОГИКА ДЛЯ КАССИРА ---
     * Оформляет заказ из текущей корзины
     */
    public boolean checkoutCurrentOrder() {
        Map<Product, Integer> cart = CartController.getCurrentCart();

        if (cart.isEmpty()) {
            System.out.println("Ошибка: Корзина пуста!");
            return false;
        }

        BigDecimal totalSum = CartController.getTotalSum();

        // Передаем данные в репозиторий для записи
        boolean success = orderRepository.saveOrder(cart, totalSum);

        if (success) {
            CartController.clearCart();
        }

        return success;
    }

    /**
     * --- ЛОГИКА ДЛЯ АДМИНИСТРАТОРА ---
     * Получает историю всех заказов для раздела "Чеки"
     */
    public List<Order> getAllOrders() {
        // Мы просто делегируем задачу репозиторию, который знает SQL
        return orderRepository.findAllOrders();
    }

    /**
     * --- ЛОГИКА ДЛЯ ЭКРАНА КЛИЕНТА ---
     * Возвращает только те заказы, которые еще не выданы (PENDING, PREPARING, READY)
     */
    public List<Order> getActiveOrders() {
        // Мы вызываем метод репозитория, который делает SELECT WHERE status IN (...)
        return orderRepository.findOrdersByStatus("PENDING", "PREPARING", "READY");
    }
}