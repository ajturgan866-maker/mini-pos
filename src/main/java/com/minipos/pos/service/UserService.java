package com.minipos.pos.service;

import com.minipos.pos.model.User;
import com.minipos.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Обязательно добавь эту аннотацию!
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Получение только активных пользователей
     */
    public List<User> getActiveUsers() {
        // Берем всех и фильтруем тех, у кого active == true
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Регистрация нового пользователя
     */
    public void register(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setActive(true); // Новый пользователь всегда активен

        userRepository.save(user); // Spring сам сделает INSERT
    }

    /**
     * Мягкое удаление (деактивация)
     */
    public void deactivateUser(User user) {
        if (user != null) {
            user.setActive(false); // Выключаем "флажок"
            userRepository.save(user); // Spring сам сделает UPDATE
        }
    }

    // Оставляем для совместимости, если где-то еще используется
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}