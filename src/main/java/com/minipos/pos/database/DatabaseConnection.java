package com.minipos.pos.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/minipos_db";

    private static final String USER = "postgres";

    private static final String PASSWORD = "1234";

    public static Connection connect() {

        try {

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Подключение успешно");

            return connection;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}