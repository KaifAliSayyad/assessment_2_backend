package com.example.trading.models;


public class UserStock {
    private Long id;
    private Stock stock;
    private Integer averageBuyPrice;
    private Integer purchasedQuantity;

    // Getters and Setters
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(Integer averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }

    public Integer getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(Integer purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}