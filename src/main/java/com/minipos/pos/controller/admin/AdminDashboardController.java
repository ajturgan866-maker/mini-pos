package com.minipos.pos.controller.admin;

import com.minipos.pos.model.Sale;
import com.minipos.pos.service.SaleService;
import com.minipos.pos.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AdminDashboardController {

    @Autowired private SaleService saleService;
    @Autowired private ReportService reportService;

    @FXML private Label lblTodaySales;
    @FXML private Label lblTotalOrders;
    @FXML private Label lblTopCategory;
    @FXML private TableView<Sale> recentSalesTable;
    @FXML private TableColumn<Sale, Integer> colId;
    @FXML private TableColumn<Sale, Double> colAmount;

    @FXML
    public void initialize() {
        setupTable();
        refreshStats();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        // Добавь остальные колонки согласно твоей модели Sale
    }

    public void refreshStats() {
        // Подтягиваем данные из БД через сервисы
        double todayTotal = saleService.getTodaySalesTotal();
        int ordersCount = saleService.getTodayOrdersCount();

        lblTodaySales.setText(String.format("%.2f сом", todayTotal));
        lblTotalOrders.setText(String.valueOf(ordersCount));

        // Загружаем последние 10 продаж в таблицу
        List<Sale> latestSales = saleService.getLatestSales(10);
        recentSalesTable.getItems().setAll(latestSales);
    }

    @FXML
    private void handleZReport() {
        // Логика из твоего старого кода
        reportService.generateZReport();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Z-Отчет");
        alert.setHeaderText(null);
        alert.setContentText("Z-отчёт успешно сформирован и сохранен в базе данных.");
        alert.showAndWait();
    }
}