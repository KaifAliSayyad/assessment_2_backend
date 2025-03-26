package com.example.demo.controller;

import java.net.Inet4Address;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.UserStock;
import com.example.demo.models.Portfolio;
import com.example.demo.models.Stocks;
import com.example.demo.service.PortfolioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
// @RequestMapping("/portfolio")
// @CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/create")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        return ResponseEntity.ok(portfolioService.createPortfolio(portfolio));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Integer userId) {
        return ResponseEntity.ok(portfolioService.getPortfolioByUserId(userId));
    }

    @PostMapping("/{userId}/watchlist")
    public ResponseEntity<Portfolio> addToWatchlist(@PathVariable Integer userId, @RequestBody Stocks stock) {
        return ResponseEntity.ok(portfolioService.addToWatchlist(userId, stock));
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<List<Stocks>> getWatchlist(@PathVariable Integer userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(portfolio.getWatchlist());
    }

    @DeleteMapping("/{userId}/watchlist/{stockId}")
    public ResponseEntity<Portfolio> removeFromWatchlist(@PathVariable Integer userId, @PathVariable Long stockId) {
        return ResponseEntity.ok(portfolioService.removeFromWatchlist(userId, stockId));
    }

    @PostMapping("/{userId}/holdings")
    public ResponseEntity<Portfolio> addToHoldings(@PathVariable Integer userId, @RequestBody UserStock stock) {
        
        return ResponseEntity.ok(portfolioService.addToHoldings(userId, stock));
    }

    @GetMapping("/{userId}/holdings")
    public ResponseEntity<List<UserStock>> getHoldings(@PathVariable Integer userId) {
        System.out.println("userId :" +userId);
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
        System.out.println("portfolio: " + portfolio);
        return ResponseEntity.ok(portfolio.getHoldings());
    }

    @DeleteMapping("/{userId}/holdings/{stockId}")
    public ResponseEntity<Portfolio> removeFromHoldings(@PathVariable Integer userId, @PathVariable Long stockId) {
        return ResponseEntity.ok(portfolioService.removeFromHoldings(userId, stockId));
    }

    @GetMapping("/{userId}/value")
    public ResponseEntity<Double> getProtfolioValue(@PathVariable Integer userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(portfolio.getValue());
    }

    @GetMapping("/{user_id}/investment")
    public Double getTotalInvestment(@PathVariable Integer user_id) {
        return portfolioService.getPortfolioTotalInvestment(user_id);
    }
    

    @GetMapping("/{userId}/{stockId}/{sellQuantity}")
    public ResponseEntity<Boolean> doesUserHasEnoughQuantity(
            @PathVariable Integer userId, 
            @PathVariable Long stockId, 
            @PathVariable Integer sellQuantity) {
        return ResponseEntity.ok(portfolioService.doesUserHasEnoughQuantity(userId, stockId, sellQuantity));
    }
    

    @Scheduled(cron = "* * * * * *")
    public void updatePortfolioValues() {
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        for (Portfolio portfolio : portfolios) {
            portfolioService.updatePortfolioValue(portfolio.getUserId());
            System.out.println("Portfolio value updated for user: " + portfolio.getUserId());
        }
    }
    
}
