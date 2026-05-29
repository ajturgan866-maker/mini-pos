package com.minipos.pos.config;

import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection connection = null;

    // Статический блок инициализации настроек из файла config.properties
    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.err.println("ВНИМАНИЕ: Файл config.properties не найден в classpath!");
            } else {
                prop.load(input);
                URL = prop.getProperty("db.url");
                USER = prop.getProperty("db.user");
                PASSWORD = prop.getProperty("db.password");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке конфигурации БД: " + e.getMessage());
        }
    }

    /**
     * Проверка доступности БД с таймаутом в 2 секунды.
     * Используется при старте программы для защиты от зависаний.
     */
    public static boolean isDatabaseReady() {
        try {
            // Проверяем текущее соединение или пробуем создать новое
            if (connection != null && !connection.isClosed()) {
                return connection.isValid(2);
            }
            // Если соединения нет, пробуем создать временное для проверки
            try (Connection testConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                return testConn.isValid(2);
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Возвращает активное подключение к базе данных.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Подключение к БД установлено.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Драйвер PostgreSQL не найден: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Закрытие соединения при завершении работы приложения.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Подключение к БД закрыто.");
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}