package com.minipos.pos.controller.shared;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HeaderController {

    @FXML private Label usernameLabel; // Имя пользователя
    @FXML private Label roleLabel;     // Роль (Админ или Кассир)
    @FXML private Label dateTimeLabel; // Текущее время

    @FXML
    public void initialize() {
        // Устанавливаем текущую дату
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dateTimeLabel.setText(dtf.format(LocalDateTime.now()));
    }

    /**
     * Метод для установки данных пользователя при входе
     */
    public void setUserInfo(String username, String role) {
        usernameLabel.setText(username);
        roleLabel.setText("[" + role + "]");
    }

    @FXML
    private void handleLogout() {
        // Логика выхода (закрытие окна или переход на Login)
        System.out.println("Пользователь вышел из системы");
        System.exit(0);
    }
}