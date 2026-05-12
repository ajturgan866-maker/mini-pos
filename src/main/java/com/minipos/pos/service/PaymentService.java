package com.minipos.pos.service;

import com.minipos.pos.model.Sale;
import com.minipos.pos.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Обязательно помечаем как сервис!
public class PaymentService {

    private final SaleRepository saleRepository;

    @Autowired // Внедряем репозиторий (никаких new!)
    public PaymentService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void processPayment(Sale sale, String cashierName) {
        try {
            // 1. Присваиваем имя кассира объекту (если оно еще не там)
            sale.setCashierName(cashierName);

            // 2. ОДНА СТРОЧКА вместо всего твоего JDBC кода:
            saleRepository.save(sale);

            // 3. Печать чека
            printReceipt(sale, cashierName);

        } catch (Exception e) {
            System.err.println("ОШИБКА ПРИ СОХРАНЕНИИ ПРОДАЖИ: " + e.getMessage());
        }
    }

    private void printReceipt(Sale sale, String cashierName) {
        System.out.println("\n---------- ЧЕК СОХРАНЕН В БД (JPA) ----------");
        System.out.println("Кассир: " + cashierName);
        System.out.println("Итого к оплате: " + sale.getTotal());
        System.out.println("---------------------------------------\n");
    }
}