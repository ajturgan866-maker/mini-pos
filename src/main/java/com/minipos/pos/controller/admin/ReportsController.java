package com.minipos.pos.controller.admin;

import com.minipos.pos.service.ReportService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component // 1. Помечаем как компонент Spring
public class ReportsController {

    @FXML private Label totalRevenueLabel;
    @FXML private Label bestCashierLabel;

    @FXML private TableView<Map.Entry<String, Double>> cashierStatsTable;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colCashierName;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colCashierRevenue;

    // 2. Внедряем сервис автоматически, никаких AppConfig
    @Autowired
    private ReportService reportService;

    @FXML
    public void initialize() {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colCashierName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getKey()));

        colCashierRevenue.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.2f", data.getValue().getValue())));
    }

    private void loadData() {
        try {
            // 3. Общая выручка
            double total = reportService.getTotalRevenue();
            totalRevenueLabel.setText(String.format("%.2f руб.", total));

            // 4. Данные по кассирам
            Map<String, Double> stats = reportService.getSalesByCashier();

            if (stats != null && !stats.isEmpty()) {
                cashierStatsTable.setItems(FXCollections.observableArrayList(stats.entrySet()));

                String best = stats.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("---");
                bestCashierLabel.setText(best);
            } else {
                cashierStatsTable.setItems(FXCollections.emptyObservableList());
                bestCashierLabel.setText("---");
            }
        } catch (Exception e) {
            // Добавляем базовую обработку ошибок, чтобы UI не падал
            System.err.println("Ошибка при загрузке отчета: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }
}