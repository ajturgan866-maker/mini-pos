package com.minipos.pos.repository;

import com.minipos.pos.config.DatabaseConfig;
import org.springframework.stereotype.Repository; // Не забудь импорт

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository // ВОТ ОНА! Теперь Spring знает, что это бин репозитория
public class ReportRepository {

    /**
     * Получает общую сумму продаж кассира за текущий день
     */
    public double getDailyTotalByCashier(String cashierName) {
        String sql = "SELECT SUM(total_amount) FROM sales " +
                "WHERE cashier_name = ? AND sale_date::date = CURRENT_DATE";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cashierName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении дневного отчета: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Получает общую выручку по всему магазину
     */
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM sales";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении общей выручки: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Получает статистику продаж: имя кассира -> сумма его продаж
     */
    public Map<String, Double> getRevenueGroupedByCashier() {
        Map<String, Double> stats = new HashMap<>();
        String sql = "SELECT cashier_name, SUM(total_amount) as total " +
                "FROM sales GROUP BY cashier_name";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("cashier_name");
                if (name == null) name = "Аноним";
                stats.put(name, rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при группировке данных по кассирам: " + e.getMessage());
        }
        return stats;
    }
}