package com.minipos.pos;

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
        // Запускаем Spring контекст
        springContext = new SpringApplicationBuilder(MiniposApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.setAppContext(springContext);

        // Получаем репозиторий пользователей
        var userRepository = springContext.getBean(com.minipos.pos.repository.UserRepository.class);

        // Если база пуста — запускаем окно первой настройки
        if (userRepository.count() == 0) {
            System.out.println(">>> База пуста. Запуск окна регистрации владельца...");
            SceneManager.switchScene("/fxml/auth/setup-view.fxml");
        } else {
            System.out.println(">>> Пользователи найдены. Переход к логину.");
            SceneManager.switchScene("/fxml/auth/login-view.fxml");
        }

        stage.setTitle("Mini-POS System");
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