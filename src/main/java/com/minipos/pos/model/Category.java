package com.minipos.pos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private List<Product> products;

    // Удобный конструктор для создания категории только по имени
    public Category(String name) {
        this.name = name;
    }
}