package com.minipos.pos.controller.admin;

import com.minipos.pos.model.User;
import com.minipos.pos.service.UserManagementService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

@Component
public class UsersController {

    private final UserManagementService userManagementService;

    @FXML private TableView<User> usersTable;

    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colPassword; // ❗ ВОТ ЭТО ТЫ ЗАБЫЛ

    public UsersController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @FXML
    public void initialize() {

        colUsername.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getUsername())
        );

        colRole.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getRole())
        );

        colPassword.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getPassword())
        );

        loadUsersData();
    }

    private void loadUsersData() {
        usersTable.getItems().setAll(userManagementService.getAllUsers());
    }

    @FXML
    private void handleAddUser() {
        System.out.println("DEBUG: Логика открытия окна добавления...");
    }
}