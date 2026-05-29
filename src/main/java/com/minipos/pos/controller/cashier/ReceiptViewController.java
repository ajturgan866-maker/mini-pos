package com.minipos.pos.controller.cashier;

import com.minipos.pos.service.ReceiptService;
import com.minipos.pos.util.I18nUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReceiptViewController {

    @FXML private TextArea receiptTextArea;
    @FXML private Button printButton; // Предположим, кнопка имеет ID printButton

    private final ReceiptService receiptService = new ReceiptService();

    @FXML
    public void initialize() {
        updateLanguage();
    }

    private void updateLanguage() {
        printButton.setText(I18nUtil.get("btn.print"));
    }

    public void initData(long orderId) {
        String receiptText = receiptService.generateReceiptText(orderId);
        receiptTextArea.setText(receiptText);
    }

    @FXML
    private void handlePrint() {
        // Логика печати остается, но добавим локализованный лог
        System.out.println(I18nUtil.get("msg.receipt.printed"));

        Stage stage = (Stage) receiptTextArea.getScene().getWindow();
        stage.close();
    }
}