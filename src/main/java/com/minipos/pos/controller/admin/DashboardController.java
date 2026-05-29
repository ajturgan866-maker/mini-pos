package com.minipos.pos.controller.admin;

import com.minipos.pos.model.Product;
import com.minipos.pos.service.ExcelImportService;
import com.minipos.pos.service.ProductService;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DashboardController {

    @FXML private Label revenueLabel;

    @FXML private TableView<Product> productsTable;

    @FXML private TableColumn<Product, Long> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colPrice;
    @FXML private TableColumn<Product, String> colImageUrl;

    private final ExcelImportService importService;
    private final ProductService productService;

    public DashboardController(ExcelImportService importService,
                               ProductService productService) {

        this.importService = importService;
        this.productService = productService;
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId())
        );

        colName.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getName())
        );

        colCategory.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCategory())
        );

        colPrice.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getPrice() != null
                                ? cell.getValue().getPrice().toString()
                                : "0"
                )
        );

        colImageUrl.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getImageUrl()
                )
        );

        updateDashboardData();
    }

    @FXML
    private void handleImportExcel(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выберите Excel файл");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Excel Files",
                        "*.xlsx",
                        "*.xls"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {

            try {

                System.out.println("DEBUG: Начинаем импорт файла:");

                importService.importProductsFromExcel(
                        selectedFile.getAbsolutePath()
                );

                System.out.println("DEBUG: Импорт завершён");

                // Обновляем таблицу
                updateDashboardData();

                System.out.println("DEBUG: Таблица обновлена");

            } catch (Exception e) {

                System.err.println("❌ Ошибка импорта Excel");

                e.printStackTrace();
            }
        }
    }

    private void updateDashboardData() {

        productsTable.getItems().clear();

        productsTable.getItems().addAll(
                productService.findAll()
        );

        productsTable.refresh();

        System.out.println(
                "DEBUG: Загружено товаров: "
                        + productsTable.getItems().size()
        );
    }
}