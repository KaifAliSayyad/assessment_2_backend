package com.example.trading.models;

public class BuyRequest {
    private Long stockId;
    private Integer quantity;

    // Getters and Setters
    public Long getStockId() { return stockId; }
    public void setStockId(Long stockId) { this.stockId = stockId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String toString(){
        return "stockId: " + stockId + " quantity: " + quantity;
    }
}

