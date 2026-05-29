package com.minipos.pos.repository;

import com.minipos.pos.config.DatabaseConfig;
import com.minipos.pos.model.Order;
import com.minipos.pos.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    /**
     * Сохраняет заказ и его содержимое в БД PostgreSQL (транзакционный метод).
     */
    public boolean saveOrder(Map<Product, Integer> cart, BigDecimal totalAmount) {
        String insertOrderSql = "INSERT INTO orders (total_amount, status, created_at) VALUES (?, 'NEW', NOW())";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            // 1. Сохраняем заголовок заказа
            orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setBigDecimal(1, totalAmount);
            orderStmt.executeUpdate();

            long orderId = -1;
            try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Не удалось получить ID заказа.");
                }
            }

            // 2. Сохраняем состав заказа
            itemStmt = conn.prepareStatement(insertItemSql);
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                itemStmt.setLong(1, orderId);
                itemStmt.setLong(2, product.getId());
                itemStmt.setInt(3, quantity);
                itemStmt.setBigDecimal(4, product.getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Ошибка БД (OrderRepository.saveOrder): " + e.getMessage());
            return false;
        } finally {
            closeResources(orderStmt, itemStmt, null);
        }
    }

    /**
     * Выбирает историю заказов для таблицы (HistoryController).
     */
    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, total_amount, created_at FROM orders ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getLong("id"),
                        rs.getBigDecimal("total_amount"), // Имя колонки теперь совпадает
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка БД (OrderRepository.findAllOrders): " + e.getMessage());
        }
        return orders;
    }

    // Вспомогательный метод для закрытия ресурсов
    private void closeResources(PreparedStatement s1, PreparedStatement s2, Connection c) {
        try {
            if (s1 != null) s1.close();
            if (s2 != null) s2.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    public List<Order> findOrdersByStatus(String... statuses) {
        List<Order> orders = new ArrayList<>();
        if (statuses.length == 0) return orders;

        // Создаем строку вида "IN (?, ?, ?)"
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < statuses.length; i++) {
            placeholders.append(i == 0 ? "?" : ", ?");
        }

        String sql = "SELECT id, total_amount, created_at, status FROM orders WHERE status IN (" + placeholders + ") ORDER BY created_at ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Заполняем параметры
            for (int i = 0; i < statuses.length; i++) {
                ps.setString(i + 1, statuses[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                            rs.getLong("id"),
                            rs.getBigDecimal("total_amount"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getString("status") // Добавь это поле в конструктор Order, если его нет
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка БД (findOrdersByStatus): " + e.getMessage());
        }
        return orders;
    }
}