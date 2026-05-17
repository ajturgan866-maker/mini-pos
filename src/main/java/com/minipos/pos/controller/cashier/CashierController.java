package com.minipos.pos.controller.cashier;

import com.minipos.pos.model.Product;
import com.minipos.pos.service.ProductService;
import com.minipos.pos.repository.SaleRepository;
import com.minipos.pos.util.AlertUtil;
import com.minipos.pos.util.QRCodeGenerator;
import com.minipos.pos.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CashierController {

    @Autowired private ProductService productService;
    @Autowired private SaleRepository saleRepository;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Product> cartList = FXCollections.observableArrayList();

    @FXML private ListView<String> categoriesListView;
    @FXML private GridPane productsGrid;
    @FXML private TableView<Product> cartTable;
    @FXML private TableColumn<Product, String> colCartName;
    @FXML private TableColumn<Product, Integer> colCartQty;
    @FXML private TableColumn<Product, Double> colCartTotal;
    @FXML private Label lblTotalAmount;
    @FXML private Label lblSubtotal;
    @FXML private TextField itemSearchField;
    @FXML private TextField barcodeScanField;

    @FXML
    public void initialize() {
        colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartTable.setItems(cartList);

        if (categoriesListView != null) {
            categoriesListView.getItems().setAll("Все товары", "Напитки", "Выпечка", "Снеки");
            categoriesListView.getSelectionModel().selectFirst();
            categoriesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                filterProductsByCategory(newVal);
            });
        }

        if (productsGrid != null) {
            productsGrid.getColumnConstraints().clear();
            for (int i = 0; i < 3; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setHgrow(Priority.SOMETIMES);
                column.setMinWidth(140);
                productsGrid.getColumnConstraints().add(column);
            }
        }

        refreshProducts();
        setupSearch();
    }

    public void refreshProducts() {
        List<Product> dbProducts = productService.findAll();
        productList.setAll(dbProducts);
        renderProductsGrid(dbProducts);
    }

    private void renderProductsGrid(List<Product> products) {
        if (productsGrid == null) return;
        productsGrid.getChildren().clear();

        int columnsCount = 3;
        int row = 0;
        int column = 0;

        for (Product product : products) {
            VBox card = new VBox();
            card.setSpacing(8);
            card.setPadding(new Insets(12));
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            card.setPrefWidth(160);

            card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: white; -fx-border-color: #2F6FED; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);"));
            card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;"));

            StackPane imageContainer = new StackPane();
            imageContainer.setPrefSize(120, 120);
            imageContainer.setMaxSize(120, 120);

            String firstLetter = (product.getName() != null && !product.getName().isEmpty())
                    ? product.getName().substring(0, 1).toUpperCase()
                    : "?";

            Label textPlaceholder = new Label(firstLetter);
            textPlaceholder.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2F6FED;");

            VBox circleContainer = new VBox(textPlaceholder);
            circleContainer.setAlignment(Pos.CENTER);
            circleContainer.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 60; -fx-min-width: 100; -fx-min-height: 100; -fx-max-width: 100; -fx-max-height: 100;");
            imageContainer.getChildren().add(circleContainer);

            String autoImageUrl = "https://images.unsplash.com/featured/120x120/?food," + product.getId();

            try {
                Image backgroundAsyncImage = new Image(autoImageUrl, 120, 120, true, true, true);
                backgroundAsyncImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                    if (newProgress.doubleValue() >= 1.0 && !backgroundAsyncImage.isError()) {
                        javafx.application.Platform.runLater(() -> {
                            ImageView imageView = new ImageView(backgroundAsyncImage);
                            imageView.setFitWidth(120);
                            imageView.setFitHeight(120);
                            imageView.setPreserveRatio(true);
                            imageContainer.getChildren().setAll(imageView);
                        });
                    }
                });
            } catch (Exception ignored) {}

            Label nameLabel = new Label(product.getName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #1E293B;");
            nameLabel.setWrapText(true);
            nameLabel.setAlignment(Pos.CENTER);

            Label priceLabel = new Label(String.format("%.2f сом", product.getPrice()));
            priceLabel.setStyle("-fx-text-fill: #2F6FED; -fx-font-weight: bold; -fx-font-size: 14;");

            card.getChildren().addAll(imageContainer, nameLabel, priceLabel);
            card.setOnMouseClicked(event -> addToCart(product));

            productsGrid.add(card, column, row);

            column++;
            if (column == columnsCount) {
                column = 0;
                row++;
            }
        }
    }

    private void setupSearch() {
        itemSearchField.textProperty().addListener((obs, old, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                renderProductsGrid(productList);
            } else {
                String query = newValue.toLowerCase();
                List<Product> filtered = productList.stream()
                        .filter(p -> p.getName().toLowerCase().contains(query))
                        .collect(Collectors.toList());
                renderProductsGrid(filtered);
            }
        });
    }

    private void filterProductsByCategory(String categoryName) {
        if (categoryName == null || "Все товары".equals(categoryName)) {
            renderProductsGrid(productList);
        } else {
            List<Product> filtered = productList.stream()
                    .filter(p -> p.getCategory() != null && categoryName.equalsIgnoreCase(p.getCategory().getName()))
                    .collect(Collectors.toList());
            renderProductsGrid(filtered);
        }
    }

    private void addToCart(Product p) {
        cartList.add(p);
        updateTotal();
    }

    private void updateTotal() {
        double total = cartList.stream().mapToDouble(Product::getPrice).sum();
        lblTotalAmount.setText(String.format("%.2f сом", total));
        lblSubtotal.setText(String.valueOf(cartList.size()));
    }

    @FXML
    public void handlePayment() {
        Window owner = lblTotalAmount.getScene().getWindow();
        if (cartList.isEmpty()) {
            AlertUtil.showError(owner, "Внимание", "Выберите товары для оплаты");
            return;
        }

        double total = cartList.stream().mapToDouble(Product::getPrice).sum();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(owner);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Выбор оплаты");
        alert.setHeaderText("К оплате: " + String.format("%.2f сом", total));

        ButtonType btnCash = new ButtonType("Наличные");
        ButtonType btnQR = new ButtonType("QR-код");
        alert.getButtonTypes().setAll(btnCash, btnQR, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(type -> {
            if (type == btnQR) {
                showQRCodeWindow(total);
            } else if (type == btnCash) {
                AlertUtil.showInfo(owner, "Успех", "Оплата наличными принята.");
                handleClearCart();
            }
        });
    }

    private void showQRCodeWindow(double amount) {
        Window owner = lblTotalAmount.getScene().getWindow();
        Stage qrStage = new Stage();

        qrStage.initModality(Modality.APPLICATION_MODAL);
        qrStage.initOwner(owner);
        qrStage.setTitle("Оплата через QR");
        qrStage.setResizable(false);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: white;");

        ImageView qrView = new ImageView(QRCodeGenerator.generateQRCodeImage("POS-PAY-" + amount));
        qrView.setFitWidth(200);
        qrView.setFitHeight(200);

        Button btnConfirm = new Button("Подтвердить оплату");
        btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;");
        btnConfirm.setOnAction(e -> {
            qrStage.close();
            handleClearCart();
            AlertUtil.showInfo(owner, "Успех", "Транзакция завершена!");
        });

        root.getChildren().addAll(new Label("Сумма: " + amount + " сом"), qrView, btnConfirm);

        Scene scene = new Scene(root, 320, 380);
        qrStage.setScene(scene);
        qrStage.centerOnScreen();
        qrStage.showAndWait();
    }

    @FXML
    public void handleClearCart() {
        cartList.clear();
        updateTotal();
    }

    @FXML
    public void showDailyReport() {
        Window owner = lblTotalAmount.getScene().getWindow();
        try {
            long totalSalesCount = saleRepository.count();
            double totalRevenue = saleRepository.findAll().stream()
                    .mapToDouble(s -> s.getTotalAmount())
                    .sum();

            String reportMessage = String.format(
                    "Успешно сформирован Z-Отчёт:\n\n" +
                            "▪ Количество чеков за смену: %d\n" +
                            "▪ Общая выручка: %.2f сом\n\n" +
                            "Смена готова к закрытию.",
                    totalSalesCount, totalRevenue
            );

            AlertUtil.showInfo(owner, "X/Z-Отчет продаж", reportMessage);
        } catch (Exception e) {
            AlertUtil.showError(owner, "Ошибка отчета", "Не удалось загрузить данные продаж из БД.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        SceneManager.switchScene("/fxml/auth/login-view.fxml");
    }

    @FXML
    public void openAdminPanel() {
        SceneManager.switchScene("/fxml/admin/admin-dashboard.fxml");
    }

    @FXML
    public void handleBarcodeScan() {
        String barcode = barcodeScanField.getText();
        if (barcode == null || barcode.trim().isEmpty()) {
            return;
        }

        Window owner = lblTotalAmount.getScene().getWindow();

        Product foundProduct = null;
        try {
            long searchId = Long.parseLong(barcode.trim());
            foundProduct = productList.stream()
                    .filter(p -> p.getId() != null && p.getId().equals(searchId))
                    .findFirst()
                    .orElse(null);
        } catch (NumberFormatException e) {
            foundProduct = null;
        }

        if (foundProduct != null) {
            addToCart(foundProduct);
            barcodeScanField.clear();
        } else {
            AlertUtil.showError(owner, "Ошибка сканера", "Товар с кодом/ID " + barcode + " не найден.");
            barcodeScanField.selectAll();
        }
    }
}