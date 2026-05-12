package com.minipos.pos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor // Нужен для Hibernate
@AllArgsConstructor // Нужен для некоторых мапперов
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_date")
    private LocalDateTime date;

    @Column(name = "total_amount")
    private Double total;

    @Column(name = "cashier_name")
    private String cashierName;

    @Column(name = "items_list", columnDefinition = "TEXT")
    private String itemsSummary;

    // --- ТОТ САМЫЙ КОНСТРУКТОР ДЛЯ SALESSERVICE ---
    public Sale(List<Product> cartItems, String cashierName) {
        this.date = LocalDateTime.now();
        this.cashierName = cashierName;

        // Считаем общую сумму
        this.total = cartItems.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        // Собираем названия товаров в одну строку для истории
        this.itemsSummary = cartItems.stream()
                .map(p -> p.getName() + " (1 шт.)")
                .collect(Collectors.joining(", "));
    }

    public Double getTotalAmount() {
        return this.total;
    }

    public void setTotalAmount(Double totalAmount) {
        this.total = totalAmount;
    }
}