package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.models.Portfolio;
import com.example.demo.models.UserStock;
import com.example.demo.models.Stocks;
import com.example.demo.repository.PortfolioRepository;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolioByUserId(Integer userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public Portfolio addToWatchlist(Integer userId, Stocks share) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        System.out.println("123: " + share);
        if (portfolio != null) {
            portfolio.getWatchlist().add(share);
            System.out.println("123: " + portfolio.getWatchlist());
            return portfolioRepository.save(portfolio);
        }
        throw new RuntimeException("Portfolio not found for user: " + userId);
    }

    public Portfolio removeFromWatchlist(Integer userId, Long stockId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio != null) {
            portfolio.getWatchlist().removeIf(share -> share.getId().equals(stockId));
            return portfolioRepository.save(portfolio);
        }
        throw new RuntimeException("Portfolio not found for user: " + userId);
    }

    public Portfolio addToHoldings(Integer userId, UserStock newHolding) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found for user: " + userId);
        }

        // Remove from watchlist if exists
        portfolio.getWatchlist().removeIf(stock -> stock.getId().equals(newHolding.getStock().getId()));

        // Check if stock already exists in holdings
        UserStock existingHolding = portfolio.getHoldings()
            .stream()
            .filter(h -> h.getStock().getId().equals(newHolding.getStock().getId()))
            .findFirst()
            .orElse(null);

        if (existingHolding != null) {
            // Update existing holding
            int totalQuantity = existingHolding.getPurchasedQuantity() + newHolding.getPurchasedQuantity();
            double totalCost = (existingHolding.getAverageBuyPrice() * existingHolding.getPurchasedQuantity()) +
                             (newHolding.getAverageBuyPrice() * newHolding.getPurchasedQuantity());
            
            existingHolding.setPurchasedQuantity(totalQuantity);
            existingHolding.setAverageBuyPrice((int) (totalCost / totalQuantity));
        } else {
            // Add new holding
            portfolio.getHoldings().add(newHolding);
        }

        // Update portfolio value
        double totalValue = portfolio.getHoldings()
            .stream()
            .mapToDouble(holding -> 
                holding.getPurchasedQuantity() * holding.getStock().getCurrentPrice())
            .sum();
        
        portfolio.setValue(totalValue);

        return portfolioRepository.save(portfolio);
    }

    public Boolean doesUserHasEnoughQuantity(Integer userId, Long stockId, Integer sellQuantity) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio == null) {
            return false;
        }

        UserStock holding = portfolio.getHoldings()
            .stream()
            .filter(h -> h.getStock().getId().equals(stockId))
            .findFirst()
            .orElse(null);

        if (holding == null) {
            return false;
        }

        return holding.getPurchasedQuantity() >= sellQuantity;
    }

    public Portfolio removeFromHoldings(Integer userId, Long stockId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found for user: " + userId);
        }

        portfolio.getHoldings().removeIf(holding -> holding.getStock().getId().equals(stockId));

        // Recalculate portfolio value after removing the holding
        double totalValue = portfolio.getHoldings()
            .stream()
            .mapToDouble(holding -> 
                holding.getPurchasedQuantity() * holding.getStock().getCurrentPrice())
            .sum();
        
        portfolio.setValue(totalValue);

        return portfolioRepository.save(portfolio);
    }
}
