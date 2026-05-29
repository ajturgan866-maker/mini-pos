package com.minipos.pos.initializer;

import com.minipos.pos.config.DatabaseConfig;

import java.sql.*;

public class ApplicationInitializer {
    public static void initializeApplication() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "role VARCHAR(20), " +
                "is_active BOOLEAN DEFAULT true)";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println(">>> База данных успешно инициализирована.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}