package com.minipos.pos.model;

/**
 * Перечисление доступных способов оплаты в Mini-POS.
 */
public enum PaymentType {
    CASH("Наличные"),
    CARD("Карта"),
    QR_CODE("QR-код");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает красивое название для интерфейса
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}