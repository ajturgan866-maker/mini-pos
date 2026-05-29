package com.minipos.pos.service;

import com.minipos.pos.config.DatabaseConfig;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
public class ReceiptService {

    /**
     * Генерирует текстовое представление чека для термопринтера на основе ID заказа
     */
    public String generateReceiptText(long orderId) {
        StringBuilder receipt = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        String orderSql = "SELECT o.order_date, o.total_amount, o.status, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.id " +
                "WHERE o.id = ?";

        String itemsSql = "SELECT p.name, oi.quantity, oi.price " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {

            // 1. Загружаем общую информацию о чеке
            Timestamp orderDate = null;
            double totalAmount = 0.0;
            String cashierName = "";

            try (PreparedStatement pstmt = conn.prepareStatement(orderSql)) {
                pstmt.setLong(1, orderId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        orderDate = rs.getTimestamp("order_date");
                        totalAmount = rs.getDouble("total_amount");
                        cashierName = rs.getString("username");
                    } else {
                        return "Ошибка: Заказ #" + orderId + " не найден в базе данных.";
                    }
                }
            }

            // 2. Шапка чека (в стиле классического фастфуд-терминала)
            receipt.append("========================================\n");
            receipt.append("             ООО \"MINI-POS\"             \n");
            receipt.append("          Точка быстрого питания        \n");
            receipt.append("========================================\n");
            receipt.append(String.format("Чек №: %-25d\n", orderId));
            receipt.append(String.format("Дата:  %-25s\n", orderDate != null ? sdf.format(orderDate) : "---"));
            receipt.append(String.format("Кассир: %-25s\n", cashierName));
            receipt.append("----------------------------------------\n");
            receipt.append("Наименование          Кол-во     Сумма  \n");
            receipt.append("----------------------------------------\n");

            // 3. Загружаем позиции из чека (Бургеры, напитки и т.д.)
            try (PreparedStatement pstmt = conn.prepareStatement(itemsSql)) {
                pstmt.setLong(1, orderId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String prodName = rs.getString("name");
                        int qty = rs.getInt("quantity");
                        double price = rs.getDouble("price");
                        double itemTotal = qty * price;

                        // Если название слишком длинное, обрезаем его для красивого выравнивания колонки
                        if (prodName.length() > 20) {
                            prodName = prodName.substring(0, 17) + "...";
                        }

                        // Форматированная строка: Название (20 символов), Количество (6 символов), Сумма
                        receipt.append(String.format("%-20s  %-6d  %7.2f\n", prodName, qty, itemTotal));
                    }
                }
            }

            // 4. Подвал чека с итоговой суммой
            receipt.append("----------------------------------------\n");
            receipt.append(String.format("ИТОГО К ОПЛАТЕ:            %11.2f сом\n", totalAmount));
            receipt.append("========================================\n");
            receipt.append("         СПАСИБО ЗА ВАШ ЗАКАЗ!          \n");
            receipt.append("             ПРИЯТНОГО АППЕТИТА!        \n");
            receipt.append("========================================\n");

        } catch (SQLException e) {
            System.err.println("Ошибка при генерации чека: " + e.getMessage());
            return "Критическая ошибка базы данных при печати чека.";
        }

        return receipt.toString();
    }
}