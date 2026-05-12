package com.minipos.pos.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReceiptWindow {

    public static void show(Long saleId, double total, String paymentType, String itemsText) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    ReceiptWindow.class.getResource("/fxml/cashier/receipt-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            com.minipos.pos.controller.cashier.ReceiptController controller =
                    loader.getController();

            controller.setData(saleId, total, paymentType, itemsText);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Receipt");
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}