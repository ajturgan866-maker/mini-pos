package com.minipos.pos.controller.admin;

import com.minipos.pos.util.I18nUtil;
import com.minipos.pos.util.ScannerManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SettingsController {

    @FXML
    private void changeToRussian() {
        I18nUtil.setLocale("ru");
        ScannerManager.setLanguage("ru");
        ScannerManager.switchScene("/fxml/admin/admin-dashboard.fxml", "Mini-POS");
    }

    @FXML
    private void changeToEnglish() {
        I18nUtil.setLocale("en");
        ScannerManager.setLanguage("en");
        ScannerManager.switchScene("/fxml/admin/admin-dashboard.fxml", "Mini-POS");
    }

    @FXML
    private void setToLightTheme() {
        ScannerManager.setTheme("light");
    }

    @FXML
    private void setToDarkTheme() {
        ScannerManager.setTheme("dark");
    }
}