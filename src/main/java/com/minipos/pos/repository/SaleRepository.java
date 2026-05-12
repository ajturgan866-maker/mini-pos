package com.minipos.pos.repository;

import com.minipos.pos.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    // Метод для получения истории, отсортированной по дате (новые сверху)
    List<Sale> findAllByOrderByDateDesc();
}