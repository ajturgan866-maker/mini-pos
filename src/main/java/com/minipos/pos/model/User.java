package com.minipos.pos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;

    // ⚠️ пароль есть, но мы контролируем доступ
    private String password;

    private String role;
    private boolean active = true;

    // конструктор для создания пользователя
    public User(String username, String password, String role, boolean active) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    // ⚠️ ВАЖНО: убираем утечку пароля в логах
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                '}';
    }
}