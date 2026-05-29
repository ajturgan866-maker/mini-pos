package com.minipos.pos.model;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private boolean inStock;

    public Product(Long id,
                   String name,
                   BigDecimal price,
                   String category,
                   String imageUrl,
                   boolean inStock) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.inStock = inStock;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public boolean isInStock() { return inStock; }
}