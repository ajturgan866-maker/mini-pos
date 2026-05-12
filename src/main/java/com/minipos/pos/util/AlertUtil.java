package com.minipos.pos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import java.util.Optional;

public class AlertUtil {

    // Вспомогательный метод для применения стилей к любому алерту
    private static void applyCustomStyle(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        // Путь к твоему CSS файлу (убедись, что путь совпадает со структурой resources)
        String cssPath = AlertUtil.class.getResource("/css/main.css").toExternalForm();
        dialogPane.getStylesheets().add(cssPath);
        dialogPane.getStyleClass().add("card"); // Используем стиль карточки из твоего CSS
    }

    // Всплывающее окно с ошибкой
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        applyCustomStyle(alert); // Добавляем стиль
        alert.showAndWait();
    }

    // Всплывающее окно с информацией (успех)
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        applyCustomStyle(alert); // Добавляем стиль
        alert.showAndWait();
    }

    // Окно подтверждения (Да/Нет)
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        applyCustomStyle(alert); // Добавляем стиль

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}