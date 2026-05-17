package com.minipos.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int stock;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    // Конструктор №1: Новый (с картинкой) — используется в ручном добавлении/кассе
    public Product(String name, double price, int stock, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Конструктор №2: СТАРАЯ ВЕРСИЯ (без картинки) — спасет твой импорт из Excel!
    public Product(String name, double price, int stock, Category category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageUrl = null; // По умолчанию картинки нет, касса сама подставит заглушку
        this.category = category;
    }
}