package com.example.demo.models;

import jakarta.persistence.*;

@Entity
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Portfolio entry ID

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stocks stock; // Reference to stock

    private Double averageBuyPrice; // For profit/loss calculation

    private Integer purchasedQuantity;

    public UserStock() {}

    public UserStock(Stocks stock, Double averageBuyPrice) {
        this.stock = stock;
        this.averageBuyPrice = averageBuyPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stocks getStock() {
        return stock;
    }

    public void setStock(Stocks stock) {
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

    public String toString(){
        return "UserStock [id=" + id + ", stock=" + stock + ", averageBuyPrice=" + averageBuyPrice + ", purchasedQuantity="
                + purchasedQuantity + "]";
    }
}
