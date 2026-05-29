package com.minipos.pos.controller.shared;

import com.minipos.pos.util.I18nUtil;
import com.minipos.pos.util.ScannerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML private void showDashboard() {
        ScannerManager.switchScene("/fxml/admin/admin-dashboard.fxml", I18nUtil.get("title.admin"));
    }
    @FXML private void showProducts() {
        ScannerManager.switchScene("/fxml/admin/products-view.fxml", I18nUtil.get("title.products"));
    }
    @FXML private void showCategories() {
        ScannerManager.switchScene("/fxml/admin/categories-view.fxml", I18nUtil.get("title.categories"));
    }
    @FXML private void showUsers() {
        ScannerManager.switchScene("/fxml/admin/users-content.fxml", I18nUtil.get("title.users"));
    }
    @FXML private void showSales() {
        ScannerManager.switchScene("/fxml/admin/sales-history-view.fxml", I18nUtil.get("title.sales"));
    }

    @FXML
    private void showSettings(ActionEvent event) {
        openSettingsModal(event);
    }

    @FXML
    private void handleLogout() {
        ScannerManager.switchScene("/fxml/auth/login-view.fxml", I18nUtil.get("title.login"));
    }

    private void openSettingsModal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/settings-view.fxml"));
            Parent root = loader.load();

            Stage settingsStage = new Stage();
            settingsStage.setTitle(I18nUtil.get("title.settings")); // Локализованный заголовок модального окна
            settingsStage.initModality(Modality.APPLICATION_MODAL);

            Node source = (Node) event.getSource();
            settingsStage.initOwner(source.getScene().getWindow());

            Scene scene = new Scene(root, 600, 450);
            settingsStage.setScene(scene);
            settingsStage.setResizable(false);
            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}