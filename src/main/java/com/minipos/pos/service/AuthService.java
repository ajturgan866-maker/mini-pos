package com.minipos.pos.service;

import com.minipos.pos.config.DatabaseConfig;
import com.minipos.pos.model.User;
import com.minipos.pos.util.PasswordHasher;
import org.springframework.stereotype.Service; // <-- 1. ДОБАВИЛИ ИМПОРТ

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service // <-- 2. ДОБАВИЛИ АННОТАЦИЮ, ЧТОБЫ SPRING УВИДЕЛ ЭТОТ КЛАСС
public class AuthService {

    public User login(String username, String password) {
        String sql = "SELECT id, username, password, role, is_active FROM users WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean isActive = rs.getBoolean("is_active");
                    String dbPassword = rs.getString("password");

                    // Сверяем хэш из базы с введенным паролем
                    if (dbPassword != null && PasswordHasher.check(password, dbPassword) && isActive) {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setUsername(rs.getString("username"));
                        user.setRole(rs.getString("role"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}