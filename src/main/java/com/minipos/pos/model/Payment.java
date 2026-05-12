package com.minipos.pos.model;

import jakarta.persistence.*; // Важно: импорт из jakarta
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // ОБЯЗАТЕЛЬНО: это делает класс "managed type"
@Table(name = "payments") // Привязка к таблице
public class Payment {

    @Id // ОБЯЗАТЕЛЬНО: первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long saleId;

    // В твоем старом репозитории было поле 'type', проверь,
    // чтобы имя поля в классе совпадало с тем, что в базе
    private String type;

    private double amount;
}