package com.minipos.pos.service;

import com.minipos.pos.model.Category;
import com.minipos.pos.model.Product;
import com.minipos.pos.model.User;
import com.minipos.pos.repository.CategoryRepository;
import com.minipos.pos.repository.ProductRepository;
import com.minipos.pos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;

// @Service
public class SetupService implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SetupService(CategoryRepository categoryRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Создаем админа
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println(">>> Создан стандартный пользователь: admin/admin123");
        }

        // 2. Создаем базовые категории
        if (categoryRepository.count() == 0) {
            Category food = new Category();
            food.setName("Еда");

            Category drinks = new Category();
            drinks.setName("Напитки");

            // Сохраняем категории
            categoryRepository.saveAll(Arrays.asList(food, drinks));

            // 3. Добавляем тестовые товары
            if (productRepository.count() == 0) {
                Product coffee = new Product();
                coffee.setName("Кофе");
                coffee.setPrice(150.0);
                // ИСПРАВЛЕНО: передаем объект drinks вместо строки "Напитки"
                coffee.setCategory(drinks);
                coffee.setStock(50);

                Product sandwich = new Product();
                sandwich.setName("Сэндвич");
                sandwich.setPrice(250.0);
                // ИСПРАВЛЕНО: передаем объект food вместо строки "Еда"
                sandwich.setCategory(food);
                sandwich.setStock(20);

                productRepository.saveAll(Arrays.asList(coffee, sandwich));
                System.out.println(">>> Базовые товары и категории добавлены!");
            }
        }
    }
}