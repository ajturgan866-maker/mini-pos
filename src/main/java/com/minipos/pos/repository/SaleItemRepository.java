package com.minipos.pos.repository;

import com.minipos.pos.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    // Здесь пока можно оставить пустым,
    // JpaRepository уже содержит стандартные методы save(), findAll() и т.д.
}