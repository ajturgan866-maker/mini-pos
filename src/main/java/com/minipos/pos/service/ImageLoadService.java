package com.minipos.pos.service; // Обязательно укажи свой пакет

import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

@Service
public class ImageLoadService {

    public Image loadImage(String url) {
        try {
            if (url == null || url.isBlank()) {
                return placeholder();
            }

            Image img = new Image(url, false); // ❗ важно: false

            if (img.isError()) {
                System.out.println("IMAGE ERROR: " + url);
                return placeholder();
            }

            return img;

        } catch (Exception e) {
            e.printStackTrace();
            return placeholder();
        }
    }

    private Image placeholder() {
        return new Image(getClass()
                .getResource("/images/placeholder.png")
                .toExternalForm());
    }
}