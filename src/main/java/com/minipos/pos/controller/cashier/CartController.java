package com.minipos.pos.controller.cashier;

import com.minipos.pos.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class CartController {

    @FXML private TableView<Product> cartTable;
    @FXML private Label totalLabel;

    // Список товаров в текущей корзине
    private final ObservableList<Product> cartItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cartTable.setItems(cartItems);
        updateTotal();
    }

    /**
     * Добавить товар в корзину
     */
    public void addToCart(Product product) {
        if (product != null) {
            cartItems.add(product);
            updateTotal();
        }
    }

    /**
     * Удалить выбранный товар из корзины
     */
    @FXML
    public void removeFromCart() {
        Product selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartItems.remove(selected);
            updateTotal();
        }
    }

    /**
     * Полная очистка корзины (после оплаты или отмены)
     */
    public void clearCart() {
        cartItems.clear();
        updateTotal();
    }

    /**
     * Пересчет итоговой суммы
     */
    private void updateTotal() {
        double total = cartItems.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        totalLabel.setText(String.format("%.2f руб.", total));
    }

    public ObservableList<Product> getCartItems() {
        return cartItems;
    }
}