package com.minipos.pos.service;

import com.minipos.pos.model.Payment;
import com.minipos.pos.model.Sale;
import com.minipos.pos.model.SaleItem;
import com.minipos.pos.repository.PaymentRepository;
import com.minipos.pos.repository.SaleItemRepository;
import com.minipos.pos.repository.SaleRepository;
import com.minipos.pos.util.ReceiptWindow;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CashierService {

    private final SaleRepository saleRepo;
    private final SaleItemRepository itemRepo;
    private final PaymentRepository paymentRepo;

    @Autowired
    public CashierService(SaleRepository saleRepo,
                          SaleItemRepository itemRepo,
                          PaymentRepository paymentRepo) {
        this.saleRepo = saleRepo;
        this.itemRepo = itemRepo;
        this.paymentRepo = paymentRepo;
    }

    /**
     * Оформление продажи (Чека)
     */
    @Transactional
    public void checkout(List<SaleItem> items, String paymentType) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Корзина пуста!");
        }

        double total = calculateTotal(items);

        // 1. Создаем и сохраняем основную запись продажи
        Sale sale = new Sale();
        sale.setDate(LocalDateTime.now());
        sale.setTotal(total);

        Sale savedSale = saleRepo.save(sale);

        // 2. Сохраняем каждую позицию из списка
        for (SaleItem item : items) {
            item.setSaleId(savedSale.getId());
            itemRepo.save(item);
        }

        // 3. Регистрируем платеж
        Payment payment = new Payment();
        payment.setSaleId(savedSale.getId());
        payment.setType(paymentType);
        payment.setAmount(total);
        paymentRepo.save(payment);

        // 4. Формируем текст и выводим чек на экран
        String receiptText = buildReceiptText(items, total);
        ReceiptWindow.show(savedSale.getId(), total, paymentType, receiptText);

        // 5. ПОСЛЕ ОПЛАТЫ: Очищаем список (чтобы подготовить к следующему клиенту)
        items.clear();
    }

    /**
     * НОВЫЙ МЕТОД: Удалить один товар из списка (чека)
     */
    public void removeItemFromCart(List<SaleItem> items, SaleItem item) {
        if (items != null && item != null) {
            items.remove(item);
        }
    }

    /**
     * НОВЫЙ МЕТОД: Полная отмена / аннулирование чека
     */
    public void clearCart(List<SaleItem> items) {
        if (items != null) {
            items.clear();
        }
    }

    /**
     * Подсчет итоговой суммы
     */
    public double calculateTotal(List<SaleItem> items) {
        if (items == null) return 0.0;
        return items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    private String buildReceiptText(List<SaleItem> items, double total) {
        StringBuilder sb = new StringBuilder("=== КАССОВЫЙ ЧЕК ===\n");
        sb.append("Дата: ").append(LocalDateTime.now()).append("\n");
        sb.append("----------------------------\n");
        for (SaleItem i : items) {
            sb.append("Товар ID: ").append(i.getProductId())
                    .append("\n  ").append(i.getQuantity()).append(" шт. x ")
                    .append(i.getPrice()).append(" = ")
                    .append(i.getPrice() * i.getQuantity()).append("\n");
        }
        sb.append("----------------------------\n");
        sb.append("ИТОГО К ОПЛАТЕ: ").append(String.format("%.2f", total)).append(" руб.");
        return sb.toString();
    }
}