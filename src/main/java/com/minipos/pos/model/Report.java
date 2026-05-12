package com.minipos.pos.model;

/**
 * Модель данных для формирования отчетов в админ-панели.
 */
public class Report {
    private String period;      // Дата или название товара
    private int salesCount;     // Количество продаж
    private double totalRevenue; // Общая сумма выручки

    // Конструктор
    public Report(String period, int salesCount, double totalRevenue) {
        this.period = period;
        this.salesCount = salesCount;
        this.totalRevenue = totalRevenue;
    }

    // Геттеры (обязательны для TableView)
    public String getPeriod() {
        return period;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    // Сеттеры (если понадобятся при расчетах)
    public void setPeriod(String period) { this.period = period; }
    public void setSalesCount(int salesCount) { this.salesCount = salesCount; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}
