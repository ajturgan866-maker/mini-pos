package com.minipos.pos.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public class SceneManager {

    private static Stage primaryStage;
    private static ConfigurableApplicationContext springContext;

    public static void setAppContext(ConfigurableApplicationContext context) {
        springContext = context;
    }

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));

            if (springContext != null) {
                loader.setControllerFactory(springContext::getBean);
            }

            Parent root = loader.load();
            Scene currentScene = primaryStage.getScene();

            if (currentScene == null) {
                currentScene = new Scene(root);
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }

            applyWindowPolicy(primaryStage, fxmlPath);

            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }

        } catch (Exception e) {
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("КРИТИЧЕСКАЯ ОШИБКА ПРИ ПЕРЕХОДЕ НА СЦЕНУ: " + fxmlPath);
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
        }
    }

    private static void applyWindowPolicy(Stage stage, String path) {
        String p = path.toLowerCase();

        if (p.contains("setup")) {
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setWidth(1200);
            stage.setHeight(750);
            stage.setMinWidth(950);
            stage.setMinHeight(650);
            stage.centerOnScreen();
            return;
        }

        if (p.contains("login")) {
            stage.setResizable(true);
            stage.setMaximized(true);
            return;
        }

        if (p.contains("admin") || p.contains("dashboard")) {
            stage.setResizable(true);
            stage.setMaximized(true);
            return;
        }

        if (p.contains("cashier")) {
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
        }
    }

    public static void openModalWindow(String fxmlPath, String title, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));

            if (springContext != null) {
                loader.setControllerFactory(springContext::getBean);
            }

            Parent root = loader.load();

            Stage modal = new Stage();
            modal.initOwner(primaryStage);
            modal.initModality(Modality.WINDOW_MODAL);

            modal.setTitle(title);
            modal.setResizable(false);

            Scene scene = new Scene(root, width, height);
            modal.setScene(scene);

            modal.setOnShown(e -> centerRelativeToOwner(modal));

            modal.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void centerRelativeToOwner(Stage modal) {
        Stage owner = (Stage) modal.getOwner();
        if (owner == null) {
            modal.centerOnScreen();
            return;
        }

        double x = owner.getX() + (owner.getWidth() - modal.getWidth()) / 2;
        double y = owner.getY() + (owner.getHeight() - modal.getHeight()) / 2;

        modal.setX(x);
        modal.setY(y);
    }
}