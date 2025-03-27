package com.example.history.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.history.models.Stock;

public interface StockRepo extends JpaRepository<Stock, Long> {
    
}
