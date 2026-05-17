package com.minipos.pos.initializer;

import com.minipos.pos.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {
        String createTablesSql = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL,
                active BOOLEAN DEFAULT TRUE
            );

            CREATE TABLE IF NOT EXISTS categories (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) UNIQUE NOT NULL
            );

            CREATE TABLE IF NOT EXISTS products (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                price NUMERIC(10,2) NOT NULL,
                stock INT NOT NULL,
                image_url VARCHAR(500),
                category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL
            );

            CREATE TABLE IF NOT EXISTS settings (
                id SERIAL PRIMARY KEY,
                initialized BOOLEAN DEFAULT FALSE
            );
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(createTablesSql);
            System.out.println(">>> [DB] Структура таблиц проверена/создана.");

            boolean isAlreadyInitialized = false;
            try (ResultSet rs = st.executeQuery("SELECT initialized FROM settings LIMIT 1")) {
                if (rs.next()) {
                    isAlreadyInitialized = rs.getBoolean("initialized");
                }
            }

            if (!isAlreadyInitialized) {
                System.out.println(">>> [DB] Первый запуск! Начинаем скачивание и генерацию товаров...");

                long categoryId = 1;
                try (PreparedStatement catSt = conn.prepareStatement(
                        "INSERT INTO categories (name) VALUES ('Напитки') ON CONFLICT DO NOTHING RETURNING id",
                        Statement.RETURN_GENERATED_KEYS)) {
                    catSt.execute();
                    try (ResultSet keys = catSt.getGeneratedKeys()) {
                        if (keys.next()) {
                            categoryId = keys.getLong(1);
                        } else {
                            try (ResultSet altKeys = st.executeQuery("SELECT id FROM categories WHERE name = 'Напитки'")) {
                                if (altKeys.next()) {
                                    categoryId = altKeys.getLong(1);
                                }
                            }
                        }
                    }
                }

                String insertProductSql = "INSERT INTO products (name, price, stock, image_url, category_id) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement prodSt = conn.prepareStatement(insertProductSql)) {
                    for (int i = 1; i <= 30; i++) {
                        prodSt.setString(1, "Товар " + i);
                        prodSt.setDouble(2, 120.0 + (i * 5));
                        prodSt.setInt(3, 15);
                        prodSt.setString(4, "https://picsum.photos/200?random=" + i);
                        prodSt.setLong(5, categoryId);
                        prodSt.addBatch();
                    }
                    prodSt.executeBatch();
                }

                st.execute("INSERT INTO settings (initialized) VALUES (TRUE)");
                System.out.println(">>> [DB] Первичная загрузка фото и товаров успешно завершена!");
            } else {
                System.out.println(">>> [DB] Товары и фото уже есть в PostgreSQL. Пропускаем загрузку, открываем окно!");
            }

        } catch (Exception e) {
            System.err.println("!!! Ошибка инициализации базы данных !!!");
            throw new RuntimeException(e);
        }
    }

    public void createTables() {
    }
}