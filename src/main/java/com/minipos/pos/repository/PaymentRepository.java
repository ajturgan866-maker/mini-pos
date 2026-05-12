package com.minipos.pos.repository;

import com.minipos.pos.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Метод save(Payment p) уже есть в JpaRepository и работает автоматически.
}