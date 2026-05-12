package com.minipos.pos.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class QRCodeGenerator {

    public static Image generateQRCodeImage(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            // Генерируем матрицу (черные и белые точки)
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            // Создаем пустое изображение JavaFX
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();

            // Попиксельно переносим данные из матрицы в картинку
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Если в матрице true — рисуем черный пиксель, иначе белый
                    pixelWriter.setColor(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return writableImage;

        } catch (Exception e) {
            System.err.println("Ошибка генерации QR-кода: " + e.getMessage());
            return null;
        }
    }
}