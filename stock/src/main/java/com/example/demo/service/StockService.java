package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.models.Stock;
import com.example.demo.repository.StockRepository;

@Service
public class StockService {

    @Autowired
    public StockRepository stockRepository;

    private final Random random = new Random();

    public Stock addStock(Stock stock) {
        stock.setCurrentPrice(generateRandomPrice(stock.getMinPrice(), stock.getMaxPrice()));
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        System.out.println("Updating stock with ID: " + id);
        return stockRepository.findById(id).map(stock -> {
            stock.setQuantity(stockDetails.getQuantity());
            stock.setMinPrice(stockDetails.getMinPrice());
            stock.setMaxPrice(stockDetails.getMaxPrice());
            stock.setCurrentPrice(stockDetails.getCurrentPrice());
            return stockRepository.save(stock);
        }).orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    public boolean isStockAvailable(Long id, int quantity) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.isPresent() && stock.get().getQuantity() >= quantity;
    }

    public Double generateRandomPrice(Double min, Double max, Double currentPrice) {
        double changePercentage = (random.nextDouble() - 0.5) * 0.02; // Generate a change between -1% to 1%
        double change = currentPrice * changePercentage;
        double newPrice = currentPrice + change;
        
        // Ensure the new price is within the min and max bounds
        newPrice = Math.max(min, Math.min(max, newPrice));
        
        return newPrice;
    }
}
