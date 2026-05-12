package com.minipos.pos.repository;

import com.minipos.pos.model.Category;
import com.minipos.pos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с товарами.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ВАРИАНТ 1: Если хочешь искать по текстовому имени категории (например, "Напитки")
    // Spring сам поймет, что нужно зайти в объект Category и проверить поле Name
    List<Product> findByCategoryName(String categoryName);

    // ВАРИАНТ 2: Если у тебя уже есть объект Category и ты хочешь найти все товары в ней
    List<Product> findByCategory(Category category);

    // Поиск по точному названию товара
    Optional<Product> findByName(String name);
}