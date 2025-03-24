package com.example.trading.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.example.trading.models.BuyRequest;
import com.example.trading.services.TradingService;

@RestController
@CrossOrigin(origins = "*")
public class TradingController {
    
    @Autowired
    private TradingService tradingService;
    
    @PostMapping("buy/{user_id}")
    public ResponseEntity<Void> buy(@PathVariable Integer user_id ,@RequestBody BuyRequest buyRequest) {
        tradingService.buy(buyRequest, user_id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("sell/{user_id}")
    public ResponseEntity<Void> sell(@PathVariable Integer user_id, @RequestBody BuyRequest sellRequest) {
        System.out.println("here....");
        tradingService.sell(sellRequest, user_id);
        return ResponseEntity.ok().build();
    }
}
