package com.minipos.pos.service;

import com.minipos.pos.config.DatabaseConfig;
import com.minipos.pos.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    /**
     * Получает список всех зарегистрированных пользователей из БД
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, role, is_active FROM users ORDER BY id ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setActive(rs.getBoolean("is_active"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка сотрудников: " + e.getMessage());
        }
        return users;
    }

    /**
     * Добавление нового сотрудника (кассира или админа)
     */
    public boolean createEmployee(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role, is_active) VALUES (?, ?, ?, TRUE)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // В реальном проекте здесь должно быть шифрование (BCrypt), для курсовой оставляем текст
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при создании сотрудника: " + e.getMessage());
            return false;
        }
    }

    /**
     * Изменение статуса активности сотрудника (Блокировка / Разблокировка)
     */
    public boolean toggleUserStatus(long userId, boolean newStatus) {
        String sql = "UPDATE users SET is_active = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, newStatus);
            stmt.setLong(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при изменении статуса сотрудника: " + e.getMessage());
            return false;
        }
    }
}