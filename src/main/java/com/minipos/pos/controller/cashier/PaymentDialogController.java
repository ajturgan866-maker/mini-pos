package com.minipos.pos.controller.cashier;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PaymentDialogController {

    @FXML private Label totalAmountLabel;    // Сколько нужно оплатить
    @FXML private TextField cashReceivedField; // Сколько дал клиент
    @FXML private Label changeLabel;         // Сдача

    private double totalAmount;
    private boolean confirmed = false;

    /**
     * Передаем сумму из основной корзины в это окно
     */
    public void setTotalAmount(double amount) {
        this.totalAmount = amount;
        totalAmountLabel.setText(String.format("%.2f руб.", amount));
    }

    /**
     * Считает сдачу при каждом изменении текста
     */
    @FXML
    private void calculateChange() {
        try {
            double received = Double.parseDouble(cashReceivedField.getText());
            double change = received - totalAmount;

            if (change >= 0) {
                changeLabel.setText(String.format("%.2f руб.", change));
                changeLabel.setStyle("-fx-text-fill: green;");
            } else {
                changeLabel.setText("Недостаточно средств");
                changeLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (NumberFormatException e) {
            changeLabel.setText("Введите число");
        }
    }

    @FXML
    private void handleConfirm() {
        try {
            double received = Double.parseDouble(cashReceivedField.getText());
            if (received >= totalAmount) {
                confirmed = true;
                closeWindow();
            }
        } catch (NumberFormatException e) {
            // Ошибка ввода
        }
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cashReceivedField.getScene().getWindow();
        stage.close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
