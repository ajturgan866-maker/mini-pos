package com.minipos.pos.controller.admin;

import com.minipos.pos.model.Category;
import com.minipos.pos.repository.CategoryRepository;
import com.minipos.pos.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @FXML private TextField nameField;
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Long> idCol;
    @FXML private TableColumn<Category, String> nameCol;

    @FXML
    public void initialize() {
        // Привязываем колонки к полям модели
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        refreshTable();
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText();
        if (name != null && !name.trim().isEmpty()) {
            categoryRepository.save(new Category(name.trim()));
            nameField.clear();
            refreshTable();
        }
    }

    @FXML
    private void handleDelete() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            categoryRepository.delete(selected);
            refreshTable();
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/fxml/admin/admin-dashboard.fxml");
    }

    private void refreshTable() {
        categoryTable.setItems(FXCollections.observableArrayList(categoryRepository.findAll()));
    }
}