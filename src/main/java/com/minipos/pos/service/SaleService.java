package com.minipos.pos.service;

import com.minipos.pos.model.Product;
import com.minipos.pos.model.Sale;
import com.minipos.pos.repository.ProductRepository;
import com.minipos.pos.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    public double getTodaySalesTotal() {
        return saleRepository.findAll().stream()
                .mapToDouble(Sale::getTotalAmount) // Теперь метод существует
                .sum();
    }

    public int getTodayOrdersCount() {
        return (int) saleRepository.count();
    }

    public List<Sale> getLatestSales(int limit) {
        return saleRepository.findAll().stream()
                .sorted((s1, s2) -> s2.getId().compareTo(s1.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional
    public void processSale(List<Product> cartItems, String cashierName) {
        if (cartItems.isEmpty()) return;

        // Создаем запись о продаже (конструктор теперь есть!)
        Sale sale = new Sale(cartItems, cashierName);
        saleRepository.save(sale);

        // Уменьшаем остатки товаров на складе
        for (Product item : cartItems) {
            productRepository.findById(item.getId()).ifPresent(product -> {
                if (product.getStock() > 0) {
                    product.setStock(product.getStock() - 1);
                    productRepository.save(product);
                }
            });
        }
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }
}