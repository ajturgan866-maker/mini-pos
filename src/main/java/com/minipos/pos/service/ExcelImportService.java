package com.minipos.pos.service;

import com.minipos.pos.config.DatabaseConfig;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class ExcelImportService {

    public void importProductsFromExcel(String filePath) {
        String sql = "INSERT INTO products (name, category, price, image_url) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (name) DO UPDATE SET price = EXCLUDED.price, image_url = EXCLUDED.image_url";

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis);
             Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Пропускаем первую строку (заголовки)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Считываем данные из ячеек
                String name = row.getCell(0).getStringCellValue();
                String category = row.getCell(1).getStringCellValue();
                double price = row.getCell(2).getNumericCellValue();
                String imageUrl = row.getCell(3).getStringCellValue();

                ps.setString(1, name);
                ps.setString(2, category);
                ps.setBigDecimal(3, java.math.BigDecimal.valueOf(price));
                ps.setString(4, imageUrl);
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("Импорт завершен успешно!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
