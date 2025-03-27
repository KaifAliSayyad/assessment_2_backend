package com.example.demo.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.History;
import com.example.demo.models.HistoryDTO;
import com.example.demo.models.Stock;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.StockRepository;

@Service
public class StockService {

    @Autowired
    public StockRepository stockRepository;

    @Autowired
    public HistoryRepository historyRepository;

    private final Random random = new Random();

    public Stock addStock(Stock stock) {
        stock.setCurrentPrice(generateRandomPrice(stock.getMinPrice(), stock.getMaxPrice(), stock.getCurrentPrice()));

        System.out.println("**************************ADDING STOCK******************************");
        Stock newStock =  stockRepository.save(stock);
        try{
            //Initializing a new history for this stock
            History newStockHistory = new History();
            newStockHistory.setName(newStock.getName());
            newStockHistory.setStockId(newStock.getId());
            newStockHistory.setMinPrice(newStock.getMinPrice());
            newStockHistory.setMaxPrice(newStock.getMaxPrice());

            historyRepository.addHistory(newStockHistory);
            System.out.println("**************************STOCK HISTORY INITIALIZED ******************************");

        }catch(Exception e){
            System.out.println("Some Error occured. Unable to Initialize histor");
            e.printStackTrace();
        }
        return newStock;
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

    public void updatePrices() {
        List<Stock> stocks = stockRepository.findAll();
        System.out.println(stocks);
        for (Stock stock : stocks) {
            Double newPrice = generateRandomPrice(stock.getMinPrice(), stock.getMaxPrice(), stock.getCurrentPrice());
            //Make  a post request to history service with body as stock id and localDate.now() and new price
            
            try{
                //Updating stock history
                HistoryDTO historydto = new HistoryDTO();
                historydto.setStockId(stock.getId());
     
                historydto.setDate(Date.from(Instant.now()));
                // historydto.setDate(java.time.LocalDate.now());
                historydto.setPrice(newPrice);
                historyRepository.saveHistory(historydto);
            }catch(Exception e){
                System.out.println("Some Error occured. Unable to Update histor");
                e.printStackTrace();
            }finally{
                stock.setCurrentPrice(newPrice);
                stockRepository.save(stock);
                System.out.println("**************************STOCK PRICE UPDATED FOR ID = "+stock.getId()+"  ******************************");
            }
        }
    }

    private Double generateRandomPrice(Double min, Double max, Double currentPrice) {
        double changePercentage = (random.nextDouble() - 0.5) * 0.02; // Generate a change between -1% to 1%
        double change = currentPrice * changePercentage;
        double newPrice = currentPrice + change;
        
        // Ensure the new price is within the min and max bounds
        newPrice = Math.max(min, Math.min(max, newPrice));
        
        return newPrice;
    }


}
