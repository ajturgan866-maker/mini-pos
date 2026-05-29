package com.minipos.pos.controller.cashier;

import com.minipos.pos.model.Product;
import com.minipos.pos.service.ImageLoadService;
import com.minipos.pos.service.ProductService;
import com.minipos.pos.service.QrCodeService;
import com.minipos.pos.util.ScannerManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class CashierOrderController {

    private final ProductService productService;
    private final QrCodeService qrCodeService;
    private final ImageLoadService imageLoadService;

    @FXML private ResourceBundle resources;

    @FXML private ListView<String> categoryList;
    @FXML private FlowPane menuGrid;

    @FXML private TableView<Product> orderTable;
    @FXML private TableColumn<Product, String> colItemName;
    @FXML private TableColumn<Product, Double> colItemPrice;

    @FXML private Label totalLabel;
    @FXML private ComboBox<String> paymentMethodBox;
    @FXML private TextField searchField;

    private List<Product> allProducts;

    public CashierOrderController(
            ProductService productService,
            QrCodeService qrCodeService,
            ImageLoadService imageLoadService
    ) {
        this.productService = productService;
        this.qrCodeService = qrCodeService;
        this.imageLoadService = imageLoadService;
    }

    @FXML
    public void initialize() {
        setupTable();
        loadData();
        setupUI();
        setupListeners();
    }

    private void setupTable() {

        colItemName.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getName() == null ? "" : d.getValue().getName()
                )
        );

        colItemPrice.setCellValueFactory(d ->
                new SimpleDoubleProperty(
                        d.getValue().getPrice() == null ? 0.0 : d.getValue().getPrice().doubleValue()
                ).asObject()
        );
    }

    private void loadData() {
        allProducts = productService.findAll();

        System.out.println("LOADED PRODUCTS: " + allProducts.size());
    }

    private void setupUI() {

        paymentMethodBox.setItems(FXCollections.observableArrayList(
                resources.getString("payment.cash"),
                resources.getString("payment.qr"),
                resources.getString("payment.card")
        ));

        loadCategories();
        render(allProducts);
        calculateTotal();
    }

    private void setupListeners() {

        categoryList.getSelectionModel()
                .selectedItemProperty()
                .addListener((o, a, b) -> filter(b));

        searchField.textProperty()
                .addListener((o, a, b) -> search(b));
    }

    private void loadCategories() {

        List<String> cats = allProducts.stream()
                .map(Product::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .collect(Collectors.toList());

        cats.add(0, resources.getString("menu.all"));

        categoryList.setItems(FXCollections.observableArrayList(cats));
        categoryList.getSelectionModel().selectFirst();
    }

    private void filter(String category) {

        if (category == null || category.equals(resources.getString("menu.all"))) {
            render(allProducts);
            return;
        }

        render(allProducts.stream()
                .filter(p -> category.equals(p.getCategory()))
                .collect(Collectors.toList()));
    }

    private void search(String q) {

        if (q == null || q.isBlank()) {
            filter(categoryList.getSelectionModel().getSelectedItem());
            return;
        }

        String s = q.toLowerCase();

        render(allProducts.stream()
                .filter(p -> p.getName() != null &&
                        p.getName().toLowerCase().contains(s))
                .collect(Collectors.toList()));
    }

    private void render(List<Product> list) {

        menuGrid.getChildren().clear();

        for (Product p : list) {

            VBox card = new VBox(8);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(10));
            card.getStyleClass().add("product-card");

            ImageView img = new ImageView(
                    imageLoadService.loadImage(p.getImageUrl())
            );

            img.setFitWidth(90);
            img.setFitHeight(70);
            img.setPreserveRatio(true);

            Label name = new Label(p.getName());

            double price = p.getPrice() == null ? 0 : p.getPrice().doubleValue();
            Label priceLabel = new Label(String.format("%.2f сом", price));

            Button add = new Button(resources.getString("btn.add"));
            add.setOnAction(e -> {
                orderTable.getItems().add(p);
                calculateTotal();
            });

            card.getChildren().addAll(img, name, priceLabel, add);
            menuGrid.getChildren().add(card);
        }
    }

    private void calculateTotal() {
        double total = orderTable.getItems().stream()
                .mapToDouble(p -> p.getPrice() == null ? 0 : p.getPrice().doubleValue())
                .sum();

        totalLabel.setText(
                resources.getString("order.total") + ": " + String.format("%.2f сом", total)
        );
    }

    @FXML
    private void handlePayment() {

        if (orderTable.getItems().isEmpty()) return;

        String method = paymentMethodBox.getValue();
        System.out.println("PAYMENT METHOD: " + method);

        if (method != null && method.toLowerCase().contains("qr")) {
            qrCodeService.generateQrImage(
                    "ORDER-" + System.currentTimeMillis(),
                    200,
                    200
            );
        }

        orderTable.getItems().clear();
        calculateTotal();
    }

    @FXML
    private void handleLogout() {
        ScannerManager.switchScene(
                "/fxml/auth/login-view.fxml",
                resources.getString("title.login")
        );
    }

    @FXML
    private void handleToggleTheme() {
        ScannerManager.toggleTheme();
    }
}