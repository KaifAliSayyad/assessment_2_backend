package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stock {
    private Long id;
    private String name;
    private Integer quantity;
    
    private Double minPrice;
    
    private Double maxPrice;
    
    private Double currentPrice;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    
    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }
    
    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }

    public String toString(){
        return "id: " + id + " name: " + name + " quantity: " + quantity + " minPrice: " + minPrice + " maxPrice: " + maxPrice + " currentPrice: " + currentPrice;
    }
}
