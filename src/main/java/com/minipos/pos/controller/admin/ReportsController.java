package com.minipos.pos.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;
// импорты...

@Component
public class ReportsController {
    @FXML private TableView<?> reportsTable;

    @FXML
    public void initialize() {
        System.out.println("Контроллер отчетов загружен!");
        // Тут должна быть загрузка данных из БД, иначе таблица пуста
    }

    @FXML private void handleExportPdf() { /* логика */ }
    @FXML private void handleRefresh() { /* логика */ }
}