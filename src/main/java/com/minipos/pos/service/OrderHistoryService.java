package com.minipos.pos.service;

import com.minipos.pos.config.DatabaseConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderHistoryService {

    // Вспомогательный класс для отображения строк чека в таблице админа
    public static class ReceiptItem {
        private final String productName;
        private final int quantity;
        private final BigDecimal price;

        public ReceiptItem(String productName, int quantity, BigDecimal price) {
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }
    }

    // Вспомогательный класс для отображения самого заказа
    public static class HistoryOrder {
        private final long id;
        private final Timestamp createdAt;
        private final BigDecimal totalAmount;
        private final String status;

        public HistoryOrder(long id, Timestamp createdAt, BigDecimal totalAmount, String status) {
            this.id = id;
            this.createdAt = createdAt;
            this.totalAmount = totalAmount;
            this.status = status;
        }

        public long getId() { return id; }
        public Timestamp getCreatedAt() { return createdAt; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }
    }

    /**
     * Загружает все заказы из базы данных для таблицы истории
     */
    public List<HistoryOrder> getOrderHistory() {
        List<HistoryOrder> history = new ArrayList<>();
        String sql = "SELECT id, created_at, total_amount, status FROM orders ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                history.add(new HistoryOrder(
                        rs.getLong("id"),
                        rs.getTimestamp("created_at"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке истории заказов: " + e.getMessage());
        }
        return history;
    }

    /**
     * Загружает состав конкретного чека по его id
     */
    public List<ReceiptItem> getOrderItems(long orderId) {
        List<ReceiptItem> items = new ArrayList<>();
        String sql = "SELECT p.name, oi.quantity, oi.price FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new ReceiptItem(
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("price")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке состава заказа: " + e.getMessage());
        }
        return items;
    }
}