package com.example.trading.models;


public class UserStock {
    private Long id;
    private Stock stock;
    private Double averageBuyPrice;
    private Integer purchasedQuantity;

    // Getters and Setters
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Double getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(Double averageBuyPrice) {
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

    public String toString(){
        return "id: " + id + " stock: " + stock + " averageBuyPrice: " + averageBuyPrice + " purchasedQuantity: " + purchasedQuantity;
    }
}