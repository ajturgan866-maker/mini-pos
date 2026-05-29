package com.minipos.pos.util;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    // Текущая тема приложения (по умолчанию светлая)
    private static String currentTheme = "light";

    // Список всех активных сцен для динамического обновления "на лету"
    private static final List<Scene> activeScenes = new ArrayList<>();

    /**
     * Регистрирует сцену в менеджере, чтобы у неё автоматически менялась тема
     */
    public static void registerScene(Scene scene) {
        if (!activeScenes.contains(scene)) {
            activeScenes.add(scene);
            applyThemeToScene(scene, currentTheme);
        }
    }

    /**
     * Удаляет сцену из менеджера (когда окно закрывается, чтобы не было утечек памяти)
     */
    public static void unregisterScene(Scene scene) {
        activeScenes.remove(scene);
    }

    /**
     * Переключает тему приложения глобально для всех открытых окон
     * @param themeName "light" или "dark"
     */
    public static void setTheme(String themeName) {
        currentTheme = themeName;
        // Проходим по всем открытым сценам JavaFX и меняем CSS файл
        for (Scene scene : activeScenes) {
            applyThemeToScene(scene, themeName);
        }
        System.out.println(">>> [ThemeManager] Установлена тема оформления: " + themeName.toUpperCase());
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Применяет конкретный CSS файл к выбранной сцене
     */
    private static void applyThemeToScene(Scene scene, String theme) {
        try {
            scene.getStylesheets().clear();

            // Базовые общие стили (шрифты, отступы, таблицы)
            String baseCss = ThemeManager.class.getResource("/css/main.css").toExternalForm();
            scene.getStylesheets().add(baseCss);

            // Специфичные стили темы оформления
            String themeCssPath = "/css/themes/" + theme + "-theme.css";
            String themeCss = ThemeManager.class.getResource(themeCssPath).toExternalForm();
            scene.getStylesheets().add(themeCss);

        } catch (Exception e) {
            System.err.println("Предупреждение [ThemeManager]: Не удалось загрузить CSS стили темы '" + theme + "'. Проверьте пути файлов.");
        }
    }
}