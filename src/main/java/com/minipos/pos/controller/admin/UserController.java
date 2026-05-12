package com.minipos.pos.controller.admin;

import com.minipos.pos.model.User;
import com.minipos.pos.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colLogin;
    @FXML private TableColumn<User, String> colPassword;
    @FXML private TableColumn<User, String> colRole;

    @FXML
    public void initialize() {
        // Заполняем выпадающий список ролей
        roleComboBox.setItems(FXCollections.observableArrayList("ADMIN", "CASHIER"));

        // Настраиваем колонки таблицы
        colLogin.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        refreshTable();
    }

    @FXML
    private void handleAddUser() {
        String login = loginField.getText();
        String pass = passwordField.getText();
        String role = roleComboBox.getValue();

        if (login != null && !login.isEmpty() && pass != null && role != null) {
            User newUser = new User();
            newUser.setUsername(login);
            newUser.setPassword(pass); // В будущем тут будет BCrypt
            newUser.setRole(role);
            newUser.setActive(true);

            userRepository.save(newUser);

            loginField.clear();
            passwordField.clear();
            refreshTable();
        } else {
            showError("Заполните все поля!");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Не даем удалить самого себя (опционально, но полезно)
            userRepository.delete(selected);
            refreshTable();
        }
    }

    private void refreshTable() {
        userTable.setItems(FXCollections.observableArrayList(userRepository.findAll()));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}