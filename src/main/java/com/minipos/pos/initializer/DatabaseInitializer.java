package com.minipos.pos.initializer;

import com.minipos.pos.config.DatabaseConfig;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseInitializer {

    public static void autoPopulateProductsFromImages() {
        try {
            URL resource = DatabaseInitializer.class.getResource("/images");
            if (resource == null) {
                System.out.println("Папка images не найдена в ресурсах!");
                return;
            }

            File folder = new File(resource.toURI());
            File[] files = folder.listFiles();
            if (files == null) return;

            // ИСПРАВЛЕНО: используем DatabaseConfig вместо DatabaseConnection
            Connection conn = DatabaseConfig.getConnection();

            String checkSql = "SELECT COUNT(*) FROM products WHERE image_path = ?";
            String insertSql = "INSERT INTO products (name, price, image_path, in_stock) VALUES (?, ?, ?, TRUE)";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            for (File file : files) {
                String fileName = file.getName();

                if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") || fileName.equals("default_food.png")) {
                    continue;
                }

                checkStmt.setString(1, fileName);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    continue;
                }

                String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
                String[] parts = nameWithoutExtension.split("_");

                StringBuilder cleanName = new StringBuilder();
                for (String part : parts) {
                    if (!part.isBlank()) {
                        cleanName.append(part.substring(0, 1).toUpperCase())
                                .append(part.substring(1))
                                .append(" ");
                    }
                }
                String finalProductName = cleanName.toString().trim();

                double price = 150.0;
                String lowerFile = fileName.toLowerCase();
                if (lowerFile.contains("pizza") || lowerFile.contains("пицца")) price = 450.0;
                else if (lowerFile.contains("ролл") || lowerFile.contains("sushi")) price = 380.0;
                else if (lowerFile.contains("burger") || lowerFile.contains("бургер")) price = 220.0;
                else if (lowerFile.contains("напиток") || lowerFile.contains("drink")) price = 70.0;

                insertStmt.setString(1, finalProductName);
                insertStmt.setBigDecimal(2, java.math.BigDecimal.valueOf(price));
                insertStmt.setString(3, fileName);
                insertStmt.executeUpdate();

                System.out.println("Автоматически добавлено в меню: " + finalProductName + " (" + price + " KGS)");
            }

            checkStmt.close();
            insertStmt.close();

        } catch (Exception e) {
            System.err.println("Ошибка автозаполнения базы данных: " + e.getMessage());
        }
    }
}