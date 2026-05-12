package com.minipos.pos.controller.shared;

import com.minipos.pos.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class SidebarController {

    @Autowired
    private ApplicationContext springContext; // Нужно, чтобы Spring управлял окном настроек

    @FXML private void showDashboard() { SceneManager.switchScene("/fxml/admin/admin-dashboard.fxml"); }
    @FXML private void showProducts() { SceneManager.switchScene("/fxml/admin/products-view.fxml"); }
    @FXML private void showCategories() { SceneManager.switchScene("/fxml/admin/categories-view.fxml"); }
    @FXML private void showUsers() { SceneManager.switchScene("/fxml/admin/users-view.fxml"); }
    @FXML private void showSales() { SceneManager.switchScene("/fxml/admin/sales-history-view.fxml"); }

    // ИЗМЕНИЛИ ЭТОТ МЕТОД
    @FXML
    private void showSettings(ActionEvent event) {
        openSettingsModal(event);
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchScene("/fxml/auth/login-view.fxml");
    }

    private void openSettingsModal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/settings-view.fxml"));

            // Сообщаем FXMLLoader, что контроллер нужно взять у Spring
            loader.setControllerFactory(springContext::getBean);

            Parent root = loader.load();

            Stage settingsStage = new Stage();
            settingsStage.setTitle("Настройки системы");

            // Блокируем основное окно, пока открыты настройки
            settingsStage.initModality(Modality.APPLICATION_MODAL);

            // Находим текущее окно, чтобы отцентрировать настройки относительно него
            Node source = (Node) event.getSource();
            settingsStage.initOwner(source.getScene().getWindow());

            // Устанавливаем сцену с четкими размерами (можешь менять под себя)
            Scene scene = new Scene(root, 600, 450);
            settingsStage.setScene(scene);

            settingsStage.setResizable(false);
            settingsStage.showAndWait(); // Ждем, пока пользователь закроет настройки

        } catch (IOException e) {
            System.err.println("Ошибка при загрузке окна настроек: " + e.getMessage());
            e.printStackTrace();
        }
    }
}