package com.minipos.pos.controller.auth;

import com.minipos.pos.service.AuthService;
import com.minipos.pos.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.authenticate(username, password)) {
            String role = authService.getUserRole(username);

            if ("ADMIN".equals(role)) {
                SceneManager.switchScene("/fxml/admin/admin-dashboard.fxml");
            } else {
                SceneManager.switchScene("/fxml/cashier/cashier-view.fxml");
            }
        } else {
            errorLabel.setText("Неверное имя пользователя или пароль");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Восстановление доступа");
        alert.setHeaderText(null);
        alert.setContentText("Пожалуйста, обратитесь к системному администратору для сброса пароля.");
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }
}