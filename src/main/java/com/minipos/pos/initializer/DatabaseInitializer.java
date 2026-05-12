package com.minipos.pos.initializer;

import com.minipos.pos.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {

        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    username VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(20) NOT NULL
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255),
                    price NUMERIC(10,2),
                    stock INT
                );
            """);

            System.out.println("DB ready");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createTables() {

        String sql = """
        CREATE TABLE IF NOT EXISTS users (
            id SERIAL PRIMARY KEY,
            username VARCHAR(255),
            password VARCHAR(255),
            role VARCHAR(50)
        );

        CREATE TABLE IF NOT EXISTS settings (
            id SERIAL PRIMARY KEY,
            initialized BOOLEAN DEFAULT FALSE
        );
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement()) {

            st.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}