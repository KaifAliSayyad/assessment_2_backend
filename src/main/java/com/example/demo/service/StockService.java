package com.example.demo.service;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.models.Stock;
import com.example.demo.repository.StockRepository;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

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
        return stockRepository.findById(id).map(stock -> {
            stock.setQuantity(stockDetails.getQuantity());
            stock.setMinPrice(stockDetails.getMinPrice());
            stock.setMaxPrice(stockDetails.getMaxPrice());
            return stockRepository.save(stock);
        }).orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    // Auto-update stock price every second
    @Scheduled(fixedRate = 1000)
    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            stock.setCurrentPrice(generateRandomPrice(stock.getMinPrice(), stock.getMaxPrice()));
            stockRepository.save(stock);
        }
    }

    private Double generateRandomPrice(Double min, Double max) {
        double newPrice = min.doubleValue() + (max.doubleValue() - min.doubleValue()) * random.nextDouble();
        return newPrice;
    }
}
