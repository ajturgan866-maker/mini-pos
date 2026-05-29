package com.minipos.pos.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class HistoryController {
    @FXML private TableView<?> historyTable;
    @FXML private TextField searchField;

    public void initialize() {
        loadHistoryData();
    }

    private void loadHistoryData() {
        // SQL запрос: SELECT * FROM orders ORDER BY created_at DESC
        System.out.println("Загрузка истории чеков из базы...");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        System.out.println("Поиск чека: " + query);
    }
}