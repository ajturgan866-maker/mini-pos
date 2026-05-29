package com.minipos.pos.controller.auth;

import com.minipos.pos.config.DatabaseConfig;
import com.minipos.pos.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class SetupController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField confirmVisibleField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    // --- ЛОГИКА ГЛАЗКА ---
    @FXML
    private void showPassword() {
        passwordVisibleField.setText(passwordField.getText());
        passwordField.setVisible(false);
        passwordVisibleField.setVisible(true);

        confirmVisibleField.setText(confirmPasswordField.getText());
        confirmPasswordField.setVisible(false);
        confirmVisibleField.setVisible(true);
    }

    @FXML
    private void hidePassword() {
        passwordField.setText(passwordVisibleField.getText());
        passwordField.setVisible(true);
        passwordVisibleField.setVisible(false);

        confirmPasswordField.setText(confirmVisibleField.getText());
        confirmPasswordField.setVisible(true);
        confirmVisibleField.setVisible(false);
    }

    // --- ЛОГИКА ЯЗЫКА ---
    @FXML
    private void changeToRussian() { ScannerManager.setLanguage("ru"); }
    @FXML
    private void changeToEnglish() { ScannerManager.setLanguage("en"); }

    // --- ЛОГИКА СОЗДАНИЯ ---
    @FXML
    private void handleSetup() {
        String username = usernameField.getText().trim();
        // Берем пароль из того поля, которое сейчас видимо
        String pass = passwordField.isVisible() ? passwordField.getText() : passwordVisibleField.getText();
        String confirm = confirmPasswordField.isVisible() ? confirmPasswordField.getText() : confirmVisibleField.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            showError(I18nUtil.get("error.fill.fields"));
            return;
        }

        if (!pass.equals(confirm)) {
            showError(I18nUtil.get("error.passwords.mismatch"));
            return;
        }

        String sql = "INSERT INTO users (username, password, role, is_active) VALUES (?, ?, 'ADMIN', true)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, PasswordHasher.hash(pass));
            pstmt.executeUpdate();

            AlertUtil.showInfo(I18nUtil.get("success.title"), I18nUtil.get("success.admin.created"));
            ScannerManager.switchScene("/fxml/auth/login-view.fxml", "Авторизация");

        } catch (SQLException e) {
            showError(I18nUtil.get("error.db") + ": " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}