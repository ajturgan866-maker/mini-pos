package com.minipos.pos.controller.cashier;

import com.minipos.pos.model.Product;
import com.minipos.pos.service.ProductService;
import com.minipos.pos.util.QRCodeGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class CashierController {

    @Autowired private ProductService productService;
    @Autowired private ApplicationContext springContext;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Product> cartList = FXCollections.observableArrayList();

    @FXML private TableView<Product> availableProductsTable;
    @FXML private TableColumn<Product, String> colItemName;
    @FXML private TableColumn<Product, Double> colItemPrice;

    @FXML private TableView<Product> cartTable;
    @FXML private TableColumn<Product, String> colCartName;
    @FXML private TableColumn<Product, Double> colCartTotal;

    @FXML private Label lblTotalAmount;
    @FXML private Label lblSubtotal;
    @FXML private TextField itemSearchField;

    @FXML
    public void initialize() {
        // Настройка таблиц
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Загрузка товаров из БД
        refreshProducts();

        // Поиск в реальном времени
        setupSearch();

        // Добавление в корзину по двойному клику
        availableProductsTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    addToCart(row.getItem());
                }
            });
            return row;
        });

        cartTable.setItems(cartList);
    }

    public void refreshProducts() {
        productList.setAll(productService.findAll());
    }

    private void setupSearch() {
        FilteredList<Product> filteredData = new FilteredList<>(productList, p -> true);
        itemSearchField.textProperty().addListener((obs, old, newValue) -> {
            filteredData.setPredicate(p -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return p.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });
        availableProductsTable.setItems(filteredData);
    }

    private void addToCart(Product p) {
        cartList.add(p);
        updateTotal();
    }

    private void updateTotal() {
        double total = cartList.stream().mapToDouble(Product::getPrice).sum();
        lblTotalAmount.setText(String.format("%.2f сом", total));
        lblSubtotal.setText(String.format("%.2f сом", total));
    }

    @FXML
    public void handlePayment() {
        if (cartList.isEmpty()) {
            showAlert("Внимание", "Выберите товары для оплаты");
            return;
        }
        double total = cartList.stream().mapToDouble(Product::getPrice).sum();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выбор оплаты");
        alert.setHeaderText("К оплате: " + String.format("%.2f сом", total));

        ButtonType btnCash = new ButtonType("Наличные");
        ButtonType btnQR = new ButtonType("QR-код");
        alert.getButtonTypes().setAll(btnCash, btnQR, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(type -> {
            if (type == btnQR) showQRCodeWindow(total);
            else if (type == btnCash) {
                showAlert("Успех", "Оплата наличными принята.");
                handleClearCart();
            }
        });
    }

    private void showQRCodeWindow(double amount) {
        Stage qrStage = new Stage();
        qrStage.initModality(Modality.APPLICATION_MODAL);
        qrStage.setTitle("Оплата через QR");

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: white;");

        ImageView qrView = new ImageView(QRCodeGenerator.generateQRCodeImage("POS-PAY-" + amount));
        qrView.setFitWidth(200); qrView.setFitHeight(200);

        Button btnConfirm = new Button("Подтвердить оплату");
        btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20;");
        btnConfirm.setOnAction(e -> {
            qrStage.close();
            handleClearCart();
            showAlert("Успех", "Транзакция завершена!");
        });

        root.getChildren().addAll(new Label("Сумма: " + amount + " сом"), qrView, btnConfirm);
        qrStage.setScene(new Scene(root));
        qrStage.show();
    }

    @FXML public void handleClearCart() { cartList.clear(); updateTotal(); }

    @FXML public void showDailyReport() { showAlert("Отчет", "Z-отчёт успешно сформирован."); }

    @FXML public void handleLogout() { loadView("/fxml/auth/login-view.fxml"); }

    @FXML public void openAdminPanel() { loadView("/fxml/admin/admin-dashboard.fxml"); }

    private void loadView(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) lblTotalAmount.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML public void showHelp() {
        showAlert("Справка", "Система Mini-POS v1.0\nРазработчики: А. Кулмамбетов и А. Белекова");
    }
}