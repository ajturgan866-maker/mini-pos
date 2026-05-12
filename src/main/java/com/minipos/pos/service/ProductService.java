package com.minipos.pos.service;

import com.minipos.pos.model.Category;
import com.minipos.pos.model.Product;
import com.minipos.pos.repository.CategoryRepository;
import com.minipos.pos.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    private final DataFormatter dataFormatter = new DataFormatter();

    // Метод, который нужен контроллеру
    public List<Product> getAllProducts() {
        return findAll();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void importFromExcel(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell nameCell = row.getCell(1);
                Cell categoryCell = row.getCell(2);
                Cell priceCell = row.getCell(4);
                Cell stockCell = row.getCell(7);

                if (nameCell != null && priceCell != null) {
                    Product product = new Product();
                    product.setName(dataFormatter.formatCellValue(nameCell));

                    if (categoryCell != null) {
                        String catName = dataFormatter.formatCellValue(categoryCell);
                        Category category = categoryRepository.findByName(catName)
                                .orElseGet(() -> {
                                    Category newCat = new Category();
                                    newCat.setName(catName);
                                    return categoryRepository.save(newCat);
                                });
                        product.setCategory(category);
                    }
                    product.setPrice(getNumericValue(priceCell));
                    product.setStock((int) getNumericValue(stockCell));
                    save(product);
                }
            }
        }
    }

    private double getNumericValue(Cell cell) {
        if (cell == null) return 0.0;
        try {
            if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
            String val = dataFormatter.formatCellValue(cell).replace(",", ".");
            return Double.parseDouble(val);
        } catch (Exception e) { return 0.0; }
    }
}