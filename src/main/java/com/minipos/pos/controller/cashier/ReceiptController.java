package com.minipos.pos.controller.cashier;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReceiptController {

    @FXML
    private Label saleIdLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label paymentTypeLabel;

    @FXML
    private TextArea itemsArea;

    private Stage stage;

    public void setData(Long saleId, double total, String paymentType, String itemsText) {

        saleIdLabel.setText("Sale ID: " + saleId);
        totalLabel.setText("Total: " + total);
        paymentTypeLabel.setText("Payment: " + paymentType);

        itemsArea.setText(itemsText);
    }

    @FXML
    public void close() {
        ((Stage) saleIdLabel.getScene().getWindow()).close();
    }
}