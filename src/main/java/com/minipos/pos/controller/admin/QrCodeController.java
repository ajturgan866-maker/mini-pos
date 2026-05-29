package com.minipos.pos.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import org.springframework.stereotype.Component;

@Component
public class QrCodeController {
    @FXML private TextField dataField;
    @FXML private ImageView qrImageView;

    @FXML
    public void handleGenerate() {
        String data = dataField.getText();
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            qrImageView.setImage(SwingFXUtils.toFXImage(MatrixToImageWriter.toBufferedImage(bitMatrix), null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}