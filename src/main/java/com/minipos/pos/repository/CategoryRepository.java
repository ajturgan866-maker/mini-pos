package com.minipos.pos.repository;

import com.minipos.pos.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Поиск категории по названию (пригодится при добавлении товара)
    Optional<Category> findByName(String name);

    // Проверка, существует ли уже такая категория
    boolean existsByName(String name);
}