package com.minipos.pos.service;

import com.minipos.pos.model.User;
import com.minipos.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Проверка логина: возвращает объект User, если пароль верен и юзер активен.
     */
    public User login(String username, String password) {
        System.out.println(">>> [AuthService] Проверка логина: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword() != null && user.getPassword().equals(password) && user.isActive()) {
                return user;
            }
        }
        return null;
    }

    /**
     * Метод для LoginController: проверяет только факт успешного входа.
     */
    public boolean authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password) && user.isActive())
                .orElse(false);
    }

    /**
     * НОВЫЙ МЕТОД: Возвращает роль пользователя по его имени.
     * Именно этот метод был "красным" в твоем LoginController.
     */
    public String getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(User::getRole) // Берет значение поля role (строку "ADMIN" или "CASHIER")
                .orElse("CASHIER"); // Если что-то пошло не так, даем роль с мин. правами
    }

    /**
     * Создание первого администратора при пустой базе.
     */
    @Transactional
    public void createFirstAdmin(String username, String password) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername(username);
            admin.setPassword(password);
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println(">>> [AuthService] Первый админ создан: " + username);
        }
    }
}