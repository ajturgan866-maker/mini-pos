package com.minipos.pos;

import com.minipos.pos.initializer.DatabaseInitializer;
import com.minipos.pos.util.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        DatabaseInitializer.init();
        springContext = new SpringApplicationBuilder(MiniposApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.setAppContext(springContext);

        // Всегда включаем окно первой настройки при запуске
        System.out.println(">>> Запуск приложения. Переход к окну первой настройки...");
        SceneManager.switchScene("/fxml/auth/setup-view.fxml");

        stage.setTitle("Mini-POS System");
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
    }

    @Override
    public void stop() {
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