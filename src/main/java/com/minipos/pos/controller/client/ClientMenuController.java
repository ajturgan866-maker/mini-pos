package com.minipos.pos.controller.client;

import com.minipos.pos.model.Product;
import com.minipos.pos.service.ImageLoadService;
import com.minipos.pos.service.ProductService;
import com.minipos.pos.util.I18nUtil;
import com.minipos.pos.util.ScannerManager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientMenuController {

    @FXML
    private GridPane menuGrid;

    private final ProductService productService;
    private final ImageLoadService imageLoadService;

    public ClientMenuController(ProductService productService,
                                ImageLoadService imageLoadService) {

        this.productService = productService;
        this.imageLoadService = imageLoadService;
    }

    @FXML
    public void initialize() {
        loadMenuCards();
    }

    private void loadMenuCards() {

        menuGrid.getChildren().clear();

        List<Product> products = productService.findAll();

        int column = 0;
        int row = 0;

        for (Product product : products) {

            VBox card = createProductCard(product);

            menuGrid.add(card, column, row);

            column++;

            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {

        VBox card = new VBox(10);

        card.setAlignment(Pos.CENTER);

        card.setPadding(new Insets(15));

        card.getStyleClass().add("product-card");

        card.setPrefWidth(220);

        // Получаем URL картинки из Excel
        String imageUrl = product.getImageUrl();

        System.out.println("IMAGE URL: " + imageUrl);

        ImageView imageView = new ImageView(
                imageLoadService.loadImage(imageUrl)
        );

        imageView.setFitWidth(180);
        imageView.setFitHeight(130);

        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(product.getName());

        nameLabel.getStyleClass().add("product-name");

        double price = product.getPrice() != null
                ? product.getPrice().doubleValue()
                : 0.0;

        Label priceLabel = new Label(
                price + " " + I18nUtil.get("currency.som")
        );

        card.getChildren().addAll(
                imageView,
                nameLabel,
                priceLabel
        );

        return card;
    }

    @FXML
    private void changeToRussian() {

        I18nUtil.setLocale("ru");

        ScannerManager.switchScene(
                "/fxml/client/client-menu.fxml",
                I18nUtil.get("app.title")
        );
    }

    @FXML
    private void changeToEnglish() {

        I18nUtil.setLocale("en");

        ScannerManager.switchScene(
                "/fxml/client/client-menu.fxml",
                I18nUtil.get("app.title")
        );
    }

    @FXML
    private void toggleTheme() {

        String current = ScannerManager
                .getTheme()
                .toLowerCase();

        String nextTheme =
                "light".equals(current)
                        ? "dark"
                        : "light";

        ScannerManager.setTheme(nextTheme);

        ScannerManager.switchScene(
                "/fxml/client/client-menu.fxml",
                I18nUtil.get("app.title")
        );
    }
}