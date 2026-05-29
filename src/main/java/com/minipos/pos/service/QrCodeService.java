package com.minipos.pos.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class QrCodeService {

    private final QRCodeWriter writer = new QRCodeWriter();

    public Image generateQrImage(String text, int width, int height) {

        try {
            BitMatrix matrix = writer.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            } catch (IOException e) {
                throw new RuntimeException("QR write failed", e);
            }

            return new Image(new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            throw new RuntimeException("QR generation failed: " + text, e);
        }
    }
}