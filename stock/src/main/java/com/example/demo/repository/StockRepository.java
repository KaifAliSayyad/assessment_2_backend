package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	 Stock findByName(String name);
}
