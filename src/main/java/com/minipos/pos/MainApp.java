package com.minipos.pos;

import com.minipos.pos.initializer.ApplicationInitializer;
import com.minipos.pos.util.ScannerManager;
import com.minipos.pos.config.DatabaseConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        ApplicationInitializer.initializeApplication();

        this.springContext = new SpringApplicationBuilder()
                .sources(MiniposApplication.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage stage) {

        if (!DatabaseConfig.isDatabaseReady()) {
            showCriticalError(
                    "База данных недоступна",
                    "Убедитесь, что PostgreSQL запущен."
            );
            return;
        }

        ScannerManager.setSpringContext(springContext);
        ScannerManager.setPrimaryStage(stage);

        String initialFxml = isFirstRun()
                ? "/fxml/auth/setup-view.fxml"
                : "/fxml/auth/login-view.fxml";

        String title = isFirstRun()
                ? "Setup"
                : "Mini-POS";

        try {
            Parent root = ScannerManager.loadFxml(initialFxml);

            Scene scene = new Scene(root);

            // =========================
            // CSS LOADING (НОРМАЛЬНО)
            // =========================
            scene.getStylesheets().add(
                    getClass().getResource("/css/base.css").toExternalForm()
            );

            scene.getStylesheets().add(
                    getClass().getResource("/css/themes/dark-theme.css").toExternalForm()
            );

            // =========================
            // THEME APPLY
            // =========================
            root.getStyleClass().removeAll("dark-theme", "light-theme");

            String theme = ScannerManager.getTheme();
            root.getStyleClass().add(theme + "-theme");

            // =========================
            // WINDOW SETTINGS (FIXED)
            // =========================
            stage.setScene(scene);
            stage.setTitle(title);

            stage.setMinWidth(1100);
            stage.setMinHeight(700);

            stage.setWidth(1280);
            stage.setHeight(800);

            stage.centerOnScreen();
            stage.setResizable(true);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showCriticalError("Ошибка запуска", e.getMessage());
        }
    }

    // =========================
    // FIRST RUN CHECK
    // =========================
    private boolean isFirstRun() {

        String sql = "SELECT COUNT(*) FROM users";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (Exception e) {
            System.err.println("Ошибка проверки пользователей: " + e.getMessage());
        }

        return true;
    }

    // =========================
    // ERROR WINDOW
    // =========================
    private void showCriticalError(String title, String content) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();

        Platform.exit();
    }

    @Override
    public void stop() {

        DatabaseConfig.closeConnection();

        if (springContext != null) {
            springContext.close();
        }

        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}