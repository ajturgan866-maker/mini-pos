package com.minipos.pos.controller.auth;

import com.minipos.pos.model.User;
import com.minipos.pos.repository.UserRepository;
import com.minipos.pos.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SetupController {

    @Autowired
    private UserRepository userRepository;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleSetup() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Заполните все поля");
            errorLabel.setVisible(true);
            return;
        }

        if (!pass.equals(confirm)) {
            errorLabel.setText("Пароли не совпадают");
            errorLabel.setVisible(true);
            return;
        }

        // Создаем первого админа
        User admin = new User();
        admin.setUsername(user);
        admin.setPassword(pass);
        admin.setRole("ADMIN");
        admin.setActive(true);

        userRepository.save(admin);

        // После успеха переходим на логин
        SceneManager.switchScene("/fxml/auth/login-view.fxml");
    }
}