package com.minipos.pos.controller.auth;

import com.minipos.pos.model.User;
import com.minipos.pos.service.AuthService;
import com.minipos.pos.util.ScannerManager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private Button toggleEyeButton;

    @FXML
    private Label errorLabel;

    @Autowired
    private AuthService authService;

    private boolean passwordVisible = false;

    @FXML
    public void initialize() {

        hideError();

        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);

        syncPasswordFields();

        // ENTER -> LOGIN
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        passwordVisibleField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }

    // =========================
    // PASSWORD VISIBILITY
    // =========================

    @FXML
    private void togglePasswordVisibility() {

        passwordVisible = !passwordVisible;

        syncPasswordFields();

        toggleEyeButton.setText(passwordVisible ? "🙈" : "👁");
    }

    private void syncPasswordFields() {

        if (passwordVisible) {

            passwordVisibleField.setText(passwordField.getText());

            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

        } else {

            passwordField.setText(passwordVisibleField.getText());

            passwordField.setVisible(true);
            passwordField.setManaged(true);

            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
        }
    }

    // =========================
    // LANGUAGE
    // =========================

    @FXML
    private void changeLanguage() {

        String newLang =
                ScannerManager.getLanguage().equals("ru")
                        ? "en"
                        : "ru";

        ScannerManager.setLanguage(newLang);
    }

    // =========================
    // THEME
    // =========================

    @FXML
    private void handleToggleTheme() {
        ScannerManager.toggleTheme();
    }

    // =========================
    // FORGOT PASSWORD (FIXED)
    // =========================

    @FXML
    private void showForgotPasswordModal() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Восстановление пароля");
        alert.setHeaderText(null);
        alert.setContentText("Обратитесь к администратору.");

        alert.showAndWait();
    }

    // =========================
    // LOGIN
    // =========================

    @FXML
    private void handleLogin() {

        hideError();

        String username = usernameField.getText().trim();

        String password =
                passwordVisible
                        ? passwordVisibleField.getText()
                        : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {

            showError(resources.getString("error.fill.fields"));
            return;
        }

        try {

            User user = authService.login(username, password);

            if (user == null) {

                showError(resources.getString("error.invalid.credentials"));
                return;
            }

            String role = user.getRole().trim().toUpperCase();

            String fxml;
            String title;

            switch (role) {

                case "ADMIN" -> {
                    fxml = "/fxml/admin/admin-dashboard.fxml";
                    title = resources.getString("title.admin");
                }

                default -> {
                    fxml = "/fxml/cashier/cashier-order.fxml";
                    title = resources.getString("title.cashier");
                }
            }

            ScannerManager.switchScene(fxml, title);

        } catch (Exception e) {

            e.printStackTrace();
            showError("System error: " + e.getMessage());
        }
    }

    // =========================
    // ERROR
    // =========================

    private void showError(String message) {

        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}