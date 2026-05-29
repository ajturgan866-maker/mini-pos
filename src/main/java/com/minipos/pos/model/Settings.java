package com.minipos.pos.model;

public class Settings {

    private Long id = 1L; // Всегда одна запись в системе для конфигурации кассы
    private String shopName;
    private String address;
    private String currency;
    private String taxNumber;

    public Settings() {}

    public Settings(String shopName, String address, String currency, String taxNumber) {
        this.shopName = shopName;
        this.address = address;
        this.currency = currency;
        this.taxNumber = taxNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTaxNumber() { return taxNumber; }
    public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
}