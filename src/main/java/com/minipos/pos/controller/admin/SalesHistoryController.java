package com.minipos.pos.controller.admin;

import com.minipos.pos.model.Sale;
import com.minipos.pos.repository.SaleRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class SalesHistoryController {

    @Autowired
    private SaleRepository saleRepository;

    @FXML private TableView<Sale> salesTable;
    @FXML private TableColumn<Sale, Long> colId;
    @FXML private TableColumn<Sale, String> colDate;
    @FXML private TableColumn<Sale, String> colItems;
    @FXML private TableColumn<Sale, Double> colAmount;
    @FXML private TableColumn<Sale, String> colCashier;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @FXML
    public void initialize() {
        // Убедись, что эти строки совпадают с именами полей в Sale.java (Lombok)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItems.setCellValueFactory(new PropertyValueFactory<>("itemsSummary"));

        // ВАЖНО: Если в модели поле называется "total", пишем "total"
        colAmount.setCellValueFactory(new PropertyValueFactory<>("total"));

        colCashier.setCellValueFactory(new PropertyValueFactory<>("cashierName"));

        // Форматирование даты (чтобы не было T в середине строки)
        colDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDate() != null) {
                return new SimpleStringProperty(cellData.getValue().getDate().format(formatter));
            }
            return new SimpleStringProperty("-");
        });

        loadHistory();
    }

    @FXML // Сделал публичным, чтобы можно было вызвать кнопку "Обновить"
    public void loadHistory() {
        try {
            // Используем метод из репозитория (убедись, что он там объявлен)
            List<Sale> history = saleRepository.findAll();

            // Если хочешь сортировку по дате (новые сверху), лучше сделать так:
            history.sort((s1, s2) -> s2.getDate().compareTo(s1.getDate()));

            salesTable.setItems(FXCollections.observableArrayList(history));
            salesTable.refresh();

        } catch (Exception e) {
            System.err.println("ОШИБКА: Не удалось загрузить историю продаж: " + e.getMessage());
            e.printStackTrace();
        }
    }
}