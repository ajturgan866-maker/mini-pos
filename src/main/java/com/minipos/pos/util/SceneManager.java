package com.minipos.pos.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext; // Оставляем этот тип

import java.io.IOException;

public class SceneManager {
    private static Stage stage;
    private static ConfigurableApplicationContext springContext; // ОСТАВИЛИ ТОЛЬКО ОДНУ

    // Метод для MainApp
    public static void setAppContext(ConfigurableApplicationContext context) {
        springContext = context;
    }

    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
    }

    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));

            // Передаем контекст Spring в FXMLLoader
            if (springContext != null) {
                loader.setControllerFactory(springContext::getBean);
            }

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка загрузки FXML: " + fxmlPath);
        }
    }
}