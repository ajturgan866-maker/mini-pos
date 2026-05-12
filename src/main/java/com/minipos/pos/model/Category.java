package com.minipos.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor // пустой конструктор Category()
@RequiredArgsConstructor // конструктор Category(String name) для полей с @NonNull
@AllArgsConstructor // конструктор Category(Long id, String name)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

}