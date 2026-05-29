package com.minipos.pos.controller.shared;

import com.minipos.pos.util.I18nUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HeaderController {

    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label dateTimeLabel;

    @FXML
    public void initialize() {
        updateDateTime();
    }

    private void updateDateTime() {
        // Формат даты может зависеть от локали
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dateTimeLabel.setText(dtf.format(LocalDateTime.now()));
    }

    /**
     * Установка данных с локализацией роли
     */
    public void setUserInfo(String username, String role) {
        usernameLabel.setText(username);

        // Локализуем роль: если в базе "ADMIN", ищем "role.admin"
        String localizedRole = I18nUtil.get("role." + role.toLowerCase());
        roleLabel.setText("[" + localizedRole + "]");
    }

    @FXML
    private void handleLogout() {
        // Здесь можно вызывать SceneManager.switchScene, как в других контроллерах
        System.out.println(I18nUtil.get("msg.logout"));
    }
}