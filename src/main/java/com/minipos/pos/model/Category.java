package com.minipos.pos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor // Нужен для Hibernate
@RequiredArgsConstructor // Создаст конструктор только для @NonNull поля (name)
@AllArgsConstructor // Для полной сборки
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false) // Добавляем констреинты на уровне БД
    private String name;


    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products;
}