package com.minipos.pos.service;

import com.minipos.pos.config.DatabaseConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    /**
     * Считает общую выручку за всё время по выданным заказам
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE status = 'DELIVERED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getBigDecimal(1) != null) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при расчете выручки: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Считает количество выданных (успешных) заказов
     */
    public int getTotalOrdersCount() {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = 'DELIVERED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при подсчете заказов: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Быстрое изменение цены товара из админки
     */
    public boolean updateProductPrice(long productId, BigDecimal newPrice) {
        String sql = "UPDATE products SET price = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newPrice);
            stmt.setLong(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при更新 цены: " + e.getMessage());
            return false;
        }
    }

    /**
     * Добавление нового блюда в меню точки быстрого питания
     */
    public boolean addProduct(String name, BigDecimal price, String imagePath) {
        String sql = "INSERT INTO products (name, price, image_path, in_stock) VALUES (?, ?, ?, TRUE)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setBigDecimal(2, price);
            stmt.setString(3, imagePath);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении товара: " + e.getMessage());
            return false;
        }
    }

    /**
     * Снятие товара с продажи (in_stock = false), чтобы он скрылся с витрины терминала
     */
    public boolean removeProductFromSale(long productId) {
        String sql = "UPDATE products SET in_stock = FALSE WHERE id = ?";
        // ИСПРАВЛЕНО: Убрано двойное объявление "Connection conn ="
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при архивации товара: " + e.getMessage());
            return false;
        }
    }

    /**
     * Получить список всех текстовых категорий для комбобокса
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения категорий: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Возвращает статистику продаж по каждому кассиру
     */
    // ИСПРАВЛЕНО: Double изменен на BigDecimal для финансовой точности без округлений
    public Map<String, BigDecimal> getSalesByCashier() {
        Map<String, BigDecimal> stats = new HashMap<>();
        String sql = "SELECT u.username, COALESCE(SUM(o.total_amount), 0) as total FROM orders o " +
                "JOIN users u ON o.user_id = u.id WHERE o.status = 'DELIVERED' GROUP BY u.username";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("username"), rs.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении статистики кассиров: " + e.getMessage());
        }
        return stats;
    }

    /**
     * Вычисляет имя лучшего кассира точки по объемам продаж
     */
    public String getBestCashierName() {
        String sql = "SELECT u.username FROM orders o JOIN users u ON o.user_id = u.id " +
                "WHERE o.status = 'DELIVERED' GROUP BY u.username ORDER BY SUM(o.total_amount) DESC LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске лучшего кассира: " + e.getMessage());
        }
        return "---";
    }
}