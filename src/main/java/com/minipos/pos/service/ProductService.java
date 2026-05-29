package com.minipos.pos.service;

import com.minipos.pos.model.Product;
import com.minipos.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Внедряем через конструктор — это лучший способ для Spring
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        // Вызываем твой метод репозитория
        return productRepository.findByInStockTrue();
    }
}