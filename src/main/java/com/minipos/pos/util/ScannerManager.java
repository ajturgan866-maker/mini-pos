package com.minipos.pos.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ScannerManager {

    private static Stage primaryStage;
    private static ApplicationContext springContext;

    private static final Preferences prefs =
            Preferences.userNodeForPackage(ScannerManager.class);

    private static String language =
            prefs.get("lang", "ru");

    private static String theme =
            prefs.get("theme", "dark");

    private static String lastFxml =
            "/fxml/auth/login-view.fxml";

    private static String lastTitle =
            "Mini-POS";

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setSpringContext(ApplicationContext context) {
        springContext = context;
    }

    // =========================
    // LANGUAGE
    // =========================

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String lang) {
        language = lang;
        prefs.put("lang", lang);
        reload();
    }

    // =========================
    // THEME
    // =========================

    public static String getTheme() {
        return theme;
    }

    public static void setTheme(String newTheme) {
        theme = newTheme;
        prefs.put("theme", theme);
        applyTheme();
    }

    public static void toggleTheme() {
        theme = theme.equals("dark") ? "light" : "dark";
        prefs.put("theme", theme);
        applyTheme();
    }

    public static void applyTheme() {

        Platform.runLater(() -> {

            if (primaryStage == null || primaryStage.getScene() == null) return;

            Scene scene = primaryStage.getScene();

            scene.getRoot().getStyleClass().removeAll("dark-theme", "light-theme");
            scene.getRoot().getStyleClass().add(theme + "-theme");
        });
    }

    // =========================
    // SCENE
    // =========================

    public static void switchScene(String fxml, String title) {

        lastFxml = fxml;
        lastTitle = title;

        Platform.runLater(() -> {

            try {

                Parent root = loadFxml(fxml);

                Scene scene = primaryStage.getScene();

                if (scene == null) {
                    scene = new Scene(root);
                    primaryStage.setScene(scene);
                } else {
                    scene.setRoot(root);
                }

                scene.getRoot().getStyleClass().removeAll("dark-theme", "light-theme");
                scene.getRoot().getStyleClass().add(theme + "-theme");

                primaryStage.setTitle(title);
                primaryStage.centerOnScreen();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // =========================
    // FXML
    // =========================

    public static Parent loadFxml(String path) throws Exception {

        URL url = ScannerManager.class.getResource(path);

        if (url == null) {
            throw new RuntimeException("FXML NOT FOUND: " + path);
        }

        FXMLLoader loader = new FXMLLoader(url);

        if (springContext != null) {
            loader.setControllerFactory(springContext::getBean);
        }

        loader.setResources(
                ResourceBundle.getBundle(
                        "lang.lang",
                        new Locale(language)
                )
        );

        return loader.load();
    }

    // =========================
    // RELOAD
    // =========================

    public static void reload() {
        switchScene(lastFxml, lastTitle);
    }
}