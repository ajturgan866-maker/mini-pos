package com.minipos.pos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.util.Optional;

public class AlertUtil {

    // Вспомогательный метод для применения стилей и фиксации окон
    private static void initCustomAlert(Alert alert, Window owner) {
        // 1. Привязка к родительскому окну (чтобы не улетало на задний план)
        if (owner != null) {
            alert.initOwner(owner);
        }

        // 2. Делаем окно модальным (блокирует клики по заднему фону кассы/админки)
        alert.initModality(Modality.APPLICATION_MODAL);

        // 3. Запрещаем пользователю растягивать границы алерта мышкой
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setResizable(false);

        // 4. Подключаем твои CSS стили
        try {
            DialogPane dialogPane = alert.getDialogPane();
            String cssPath = AlertUtil.class.getResource("/css/main.css").toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            dialogPane.getStyleClass().add("card");
        } catch (Exception e) {
            System.err.println("Предупреждение: Не удалось загрузить main.css для Alert. Проверь путь.");
        }
    }

    // Всплывающее окно с ошибкой (owner передаем из контроллера через node.getScene().getWindow())
    public static void showError(Window owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        initCustomAlert(alert, owner);
        alert.showAndWait();
    }

    // Всплывающее окно с информацией (успех)
    public static void showInfo(Window owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        initCustomAlert(alert, owner);
        alert.showAndWait();
    }

    // Окно подтверждения (Да/Нет)
    public static boolean showConfirmation(Window owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        initCustomAlert(alert, owner);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Перегруженные методы БЕЗ owner (на случай, если окно вызывается из фонового потока/сервиса)
    public static void showError(String title, String message) { showError(null, title, message); }
    public static void showInfo(String title, String message) { showInfo(null, title, message); }
    public static boolean showConfirmation(String title, String message) { return showConfirmation(null, title, message); }
}