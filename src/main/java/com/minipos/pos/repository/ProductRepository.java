package com.minipos.pos.repository;

import com.minipos.pos.config.DatabaseConfig;
import com.minipos.pos.model.Product;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    public List<Product> findByInStockTrue() {
        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT id, name, price, category, image_url, in_stock
            FROM products
            WHERE in_stock = TRUE
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Product product = new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("category"),   // ✔ теперь правильно
                        rs.getString("image_url"),  // ✔ совпадает с моделью
                        rs.getBoolean("in_stock")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении findByInStockTrue: " + e.getMessage());
        }

        return products;
    }
}