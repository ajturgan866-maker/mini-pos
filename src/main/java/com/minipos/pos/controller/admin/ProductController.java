package com.minipos.pos.controller.admin;

import com.minipos.pos.model.Category;
import com.minipos.pos.model.Product;
import com.minipos.pos.repository.CategoryRepository;
import com.minipos.pos.repository.ProductRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*; // Оставляем для Workbook и Sheet
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colCategory;

    private final DataFormatter dataFormatter = new DataFormatter();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colCategory.setCellValueFactory(cellData -> {
            Category cat = cellData.getValue().getCategory();
            return new SimpleStringProperty(cat != null ? cat.getName() : "—");
        });

        refreshTable();
    }

    @FXML
    private void handleImportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файла для импорта");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        File file = fileChooser.showOpenDialog(productTable.getScene().getWindow());

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;

                    // Явно указываем полное имя класса POI Cell, чтобы не было конфликта с JavaFX
                    String name = getCellValue(row.getCell(0));
                    String priceStr = getCellValue(row.getCell(1)).replace(",", ".");
                    String stockStr = getCellValue(row.getCell(2));
                    String catName = getCellValue(row.getCell(3));

                    if (name.isEmpty()) continue;

                    double price = Double.parseDouble(priceStr.isEmpty() ? "0" : priceStr);
                    int stock = (int) Double.parseDouble(stockStr.isEmpty() ? "0" : stockStr);

                    Category category = categoryRepository.findAll().stream()
                            .filter(c -> c.getName().equalsIgnoreCase(catName))
                            .findFirst()
                            .orElseGet(() -> categoryRepository.save(new Category(catName)));

                    productRepository.save(new Product(name, price, stock, category));
                }

                refreshTable();
                showAlert("Успех", "Данные импортированы!", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Здесь тоже указываем полное имя класса для аргумента
    private String getCellValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) return "";
        return dataFormatter.formatCellValue(cell).trim();
    }

    private void refreshTable() {
        List<Product> products = productRepository.findAll();
        productTable.setItems(FXCollections.observableArrayList(products));
    }

    private void showAlert(String title, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}