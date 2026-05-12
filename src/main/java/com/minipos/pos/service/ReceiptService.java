package com.minipos.pos.service;

public class ReceiptService {

    public String generateReceipt(Long saleId, double total) {

        return """
                ===== MINI POS =====
                Sale ID: %d
                --------------------
                TOTAL: %.2f
                ====================
                """.formatted(saleId, total);
    }
}