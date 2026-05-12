package com.minipos.pos.initializer;

import com.minipos.pos.repository.SettingsRepository;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component // Позволяет Spring управлять этим классом
public class ApplicationInitializer {

    private final SettingsRepository settingsRepository;

    // Spring сам передаст сюда нужный объект (Bean) при запуске
    public ApplicationInitializer(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @PostConstruct // Этот метод выполнится автоматически СРАЗУ после запуска приложения
    public void init() {
        // Проверяем, есть ли настройки в базе
        if (settingsRepository.count() == 0) {
            System.out.println(">>> Инициализация: Настройки не найдены, создаем дефолтные...");
            // Здесь можно вызвать databaseInitializer или просто сохранить пустые настройки
        }
    }
}