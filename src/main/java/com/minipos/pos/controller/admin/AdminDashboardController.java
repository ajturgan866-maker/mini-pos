package com.minipos.pos.controller.admin;

import com.minipos.pos.util.ScannerManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

@Component
public class AdminDashboardController {

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        Platform.runLater(this::showDashboard);
    }

    private void loadView(String fxml) {
        try {
            if (contentArea == null) {
                System.err.println("❌ Ошибка: contentArea равен null!");
                return;
            }

            // ScannerManager.loadFxml уже делает applyThemeToNode(view) внутри себя.
            // Тебе не нужно вручную удалять/добавлять классы тем.
            Parent view = ScannerManager.loadFxml("/fxml/admin/" + fxml);

            // Просто меняем содержимое.
            // ScannerManager сам применит нужные CSS-классы к загруженному view.
            contentArea.getChildren().setAll(view);

            System.out.println("DEBUG: Успешно загружена вкладка: " + fxml);

        } catch (Exception e) {
            System.err.println("❌ Не удалось загрузить FXML: /fxml/admin/" + fxml);
            e.printStackTrace();
        }
    }

    @FXML private void showDashboard() { loadView("dashboard-content.fxml"); }
    @FXML private void showHistory()   { loadView("history-content.fxml"); }
    @FXML private void showUsers()     { loadView("users-content.fxml"); }
    @FXML private void showSettings()  { loadView("settings-content.fxml"); }
    @FXML private void showReports()   { loadView("reports-content.fxml"); }

    @FXML
    private void handleLogout() {
        ScannerManager.switchScene("/fxml/auth/login-view.fxml", "Mini-POS Login");
    }
}