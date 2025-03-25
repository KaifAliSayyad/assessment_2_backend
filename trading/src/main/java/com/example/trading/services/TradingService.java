package com.example.trading.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.trading.models.BuyRequest;
import com.example.trading.models.Stock;
import com.example.trading.models.UserStock;

@Service
public class TradingService {
    
    @Value("${register.service.url}")
    private String registerServiceUrl;
    
    @Value("${stock.service.url}")
    private String stockServiceUrl;
    
    @Value("${portfolio.service.url}")
    private String portfolioServiceUrl;
    
    private final RestTemplate restTemplate;

    public TradingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public void buy(BuyRequest buyRequest, Integer user_id) {
        System.out.println(buyRequest);
        System.out.println(user_id);
        try {
            // Validate input
            if (buyRequest.getStockId() == null || user_id == null || buyRequest.getQuantity() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "StockId, userId, and quantity are required");
            }

            if (buyRequest.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Quantity must be greater than 0");
            }
            String url = stockServiceUrl + "/" + buyRequest.getStockId();
            System.out.println(url);
            // 1. Get stock details from stock service
            Stock stock = restTemplate.getForObject(
                url,
                Stock.class
            );
            
            System.out.println(stock);
            if (stock == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Stock not found");
            }
            
            // 2. Check if requested quantity is available
            if (buyRequest.getQuantity() > stock.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Requested quantity exceeds available stock quantity");
            }
            
            // 3. Calculate total cost
            double totalCost = buyRequest.getQuantity() * stock.getCurrentPrice();
            System.out.println("totalCost: " + totalCost);
            // 4. Check user balance
            Double userBalance = restTemplate.getForObject(
                registerServiceUrl + "/" + "balance/" + user_id,
                Double.class
            );
            System.out.println("userBalance: " + userBalance);
            
            if (userBalance == null || userBalance < totalCost) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Insufficient balance to complete the purchase");
            }
            
            // 5. Create UserStock object for portfolio
            UserStock userStock = new UserStock();
            userStock.setStock(stock);
            userStock.setAverageBuyPrice(stock.getCurrentPrice());
            userStock.setPurchasedQuantity(buyRequest.getQuantity());
            String portfolioUrl = portfolioServiceUrl + "/" + user_id + "/holdings";
            System.out.println(portfolioUrl);
            // 6. Add stock to user's portfolio
            ResponseEntity<Void> response = restTemplate.postForEntity(
                portfolioUrl,
                userStock,
                Void.class
            );
            System.out.println(response);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to update user holdings");
            }

            // 7. Update user balance
            Double newBalance = userBalance - totalCost;
            ResponseEntity<Void> response2 = restTemplate.postForEntity(
                registerServiceUrl + "/balance/" + user_id,
                newBalance,
                Void.class
            );

            System.out.println("newBalance: " +response2 );
            // 8. Update stock quantity
            int newQuantity = stock.getQuantity() - buyRequest.getQuantity();
            stock.setQuantity(newQuantity);
            restTemplate.put(
                stockServiceUrl + "/" + stock.getId(),
                stock
            );
            

        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "An error occurred while processing the purchase: " + e.getMessage());
        }
    }

    public void sell(BuyRequest sellRequest, Integer user_id) {
        System.out.println("Sell request: " + sellRequest);
        System.out.println("User ID: " + user_id);
        try {
            // Validate input
            if (sellRequest.getStockId() == null || user_id == null || sellRequest.getQuantity() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "StockId, userId, and quantity are required");
            }

            if (sellRequest.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Quantity must be greater than 0");
            }

            // Define URLs
            String getStockUrl = stockServiceUrl + "/" + sellRequest.getStockId();
            String getUserHoldingsUrl = portfolioServiceUrl + "/" + user_id + "/holdings";
            String updateHoldingsUrl = portfolioServiceUrl + "/" + user_id + "/holdings";
            String updateBalanceUrl = registerServiceUrl + "/balance/" + user_id;
            String updateStockUrl = stockServiceUrl + "/" + sellRequest.getStockId();       //This is PUT request

            // 1. Get stock details from stock service
            Stock stock = restTemplate.getForObject(
                getStockUrl,
                Stock.class
            );

            System.out.println("Stock  "+stock);
            if (stock == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found");
            }

            System.out.println(getUserHoldingsUrl+ " getUserHoldingsUrl");
            // 2. Check user's holdings
            // List<UserStock> userHoldings = restTemplate.getForObject(
            //     getUserHoldingsUrl,
            //     List.class
            // );

            UserStock[] userHoldings = restTemplate.getForObject(
                getUserHoldingsUrl,
                UserStock[].class
            );
            
            // System.out.println(userHoldings.size());
            // for(int i = 0; i < userHoldings.size(); i++){
            //     userHoldings.get(0).getClass();
            // }
            // for(UserStock obj : userHoldings){
            //     System.out.println(obj.getStock().getId() + "obj.getStock().getId()");
            //     System.out.println(obj.getAverageBuyPrice() + "obj.getAverageBuyPrice()");
            //     System.out.println(obj.getStock().getCurrentPrice() + "obj.getStock().getCurrentPrice()");
            //     System.out.println(obj.getStock().getQuantity() + "obj.getStock().getQuantity()");
            //     System.out.println(obj.getStock().getName() + "obj.getStock().getStockName()");
            //     System.out.println(obj.getPurchasedQuantity() + "obj.getPurchasedQuantity()");
            // }
            // UserStock userStock = userHoldings.stream().filter((UserStock obj) -> obj.getStock().getId() == sellRequest.getStockId()).collect(Collectors.toList()).get(0);
            // System.out.println(userStock + "userStock");
            UserStock reqStock = Arrays.asList(userHoldings).stream().filter(h -> h.getStock().getId().equals(sellRequest.getStockId())).collect(Collectors.toList()).get(0);
            System.out.println(reqStock + "reqStock");
            System.out.println(sellRequest.getQuantity() + "sellRequest.getQuantity()");
            // System.out.println(userHoldings.get(0).getPurchasedQuantity() + "userHoldings.get(0).getPurchasedQuantity()");
            System.out.println(stock.getCurrentPrice() + "stock.getCurrentPrice()");

            if (userHoldings.length == 0 || reqStock.getPurchasedQuantity() < sellRequest.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Insufficient stock holdings");
            }

            System.out.println("Came here");


            // 3. Calculate sell value
            double sellValue = sellRequest.getQuantity() * stock.getCurrentPrice();



            System.out.println("SellValue  "+sellValue);

            // 4. Update user's holdings

            System.out.println("purchased quantity" + reqStock.getPurchasedQuantity());
            int remainingQuantity = reqStock.getPurchasedQuantity() - sellRequest.getQuantity();
            System.out.println("remainingQuantity  "+remainingQuantity);
            if (remainingQuantity == 0) {
                // Delete the holding if no stocks remain
                restTemplate.delete(updateHoldingsUrl + "/" + sellRequest.getStockId());
            } else {
                // Update the holding with new quantity
                reqStock.setPurchasedQuantity(remainingQuantity);
                System.out.println("************************updated user holding******************: "+reqStock.toString());
                restTemplate.postForObject(
                    updateHoldingsUrl,
                    reqStock,
                    Void.class
                );
            }

            // 5. Update user's balance
            Double currentBalance = restTemplate.getForObject(
                registerServiceUrl + "/balance/" + user_id,
                Double.class
            );
            
            if (currentBalance == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User balance not found");
            }

            Double newBalance = currentBalance + sellValue;
            ResponseEntity<Void> balanceResponse = restTemplate.postForEntity(
                updateBalanceUrl,
                newBalance,
                Void.class
            );

            if (!balanceResponse.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to update user balance");
            }

            // 6. Update stock quantity in stock service
            int newStockQuantity = stock.getQuantity() + sellRequest.getQuantity();
            stock.setQuantity(newStockQuantity);
            restTemplate.put(
                updateStockUrl,
                stock
            );

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.out.println("HttpClientErrorException");
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "An error occurred while processing the sale: " + e.getMessage());
        }
    }
}
