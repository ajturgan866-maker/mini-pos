package com.minipos.pos.util;

import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    // Кэш картинок, чтобы не читать их с диска по сто раз
    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final String DEFAULT_IMAGE = "/images/default_food.png";

    public static Image getProductImage(String imageName) {
        // Если имя картинки не задано в БД, сразу возвращаем заглушку
        if (imageName == null || imageName.isBlank()) {
            return loadFromResources(DEFAULT_IMAGE);
        }

        // Если картинка уже загружалась ранее, выдаем из памяти
        if (imageCache.containsKey(imageName)) {
            return imageCache.get(imageName);
        }

        String fullPath = "/images/" + imageName;
        Image image = loadFromResources(fullPath);

        // Если файла в папке resources/images/ нет, используем заглушку
        if (image == null) {
            System.out.println("Предупреждение: Файл " + fullPath + " не найден. Используется заглушка.");
            image = loadFromResources(DEFAULT_IMAGE);
        }

        // Сохраняем в кэш и возвращаем
        imageCache.put(imageName, image);
        return image;
    }

    private static Image loadFromResources(String path) {
        try {
            InputStream is = ImageLoader.class.getResourceAsStream(path);
            if (is != null) {
                return new Image(is);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении ресурса " + path + ": " + e.getMessage());
        }
        return null;
    }
}