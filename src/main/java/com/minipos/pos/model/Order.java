package com.minipos.pos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private final long id;
    private final BigDecimal totalSum;
    private final LocalDateTime createdAt;
    private final String status; // Новое поле

    // Основной конструктор со статусом
    public Order(long id, BigDecimal totalSum, LocalDateTime createdAt, String status) {
        this.id = id;
        this.totalSum = totalSum;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Дополнительный конструктор (для обратной совместимости, если статус неизвестен)
    public Order(long id, BigDecimal totalSum, LocalDateTime createdAt) {
        this(id, totalSum, createdAt, "UNKNOWN");
    }

    public long getId() { return id; }
    public BigDecimal getTotalSum() { return totalSum; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; } // Новый геттер
}