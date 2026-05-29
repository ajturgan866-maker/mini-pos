package com.minipos.pos.controller.admin;

import com.minipos.pos.util.I18nUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class MenuManagementController {

    // Элементы UI
    @FXML private Label titleLabel; // Убедись, что в FXML у label есть fx:id="titleLabel"
    @FXML private Button addBtn;
    @FXML private Button deleteBtn;

    // Таблица
    @FXML private TableView<?> menuTable;
    @FXML private TableColumn<Object, String> colName;
    @FXML private TableColumn<Object, Double> colPrice;

    @FXML
    public void initialize() {
        // 1. Обновляем все тексты через ключи
        updateUI();

        // 2. Здесь нужно будет загрузить данные из БД (как только будет готов сервис)
        loadMenuData();
    }

    private void updateUI() {
        // Если в FXML у элементов стоят ключи %, они сами подтянутся.
        // Но если хочешь через код:
        if (titleLabel != null) titleLabel.setText(I18nUtil.get("menu.management.title"));
        if (addBtn != null) addBtn.setText(I18nUtil.get("btn.add.product"));
        if (deleteBtn != null) deleteBtn.setText(I18nUtil.get("btn.delete.product"));
    }

    private void loadMenuData() {
        // ВАЖНО: Если тут пусто, значит сервис не возвращает данные
        System.out.println("Загрузка товаров из БД...");
        // menuTable.setItems(...);
    }

    @FXML
    private void handleAddProduct() { /* Логика добавления */ }

    @FXML
    private void handleDeleteProduct() { /* Логика удаления */ }

    @FXML
    private void handleImportExcel() {
        // Тут мы вызовем наш будущий парсер Excel
        System.out.println("Запуск импорта...");
    }
}