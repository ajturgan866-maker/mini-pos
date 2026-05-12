package com.minipos.pos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sale_items")
@Data
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long saleId;
    private Long productId;

    private Integer quantity; // Должно быть так для getQuantity()
    private Double price;     // Должно быть так для getPrice()
}