package com.minipos.pos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;

    private boolean active = true;

    // Ручные геттеры, сеттеры и конструкторы удалены.
    // Lombok (@Data и @AllArgsConstructor) создаст их автоматически.
    // Это позволит SetupService вызывать new User(null, "admin", "admin123", "ADMIN") без ошибок.
}