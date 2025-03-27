package com.example.history.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.history.models.History;
import com.example.history.models.HistoryDTO;
import com.example.history.services.HistoryService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
// @RequestMapping("/history")
// @CrossOrigin(origins = "*")
public class HistoryController {
    
    @Autowired
    private HistoryService service;

    @PostMapping("/add")
    public ResponseEntity<String> initializeHistory(@RequestBody History body ) {
        return (service.initializeHistory(body) instanceof History)? ResponseEntity.ok("History initialized successfully") : ResponseEntity.badRequest().body("Failed to initialize history");
    }
    

    @PostMapping
    public ResponseEntity<String> writeHistory(@RequestBody HistoryDTO body) {
        History updatedHistory = service.saveHistory(body);
        if(updatedHistory != null){
            System.out.println(updatedHistory);
            return ResponseEntity.ok("History updated successfully");
        }
        return ResponseEntity.badRequest().body("Failed to update history");
    }

    @GetMapping
    public List<History> getAllHistories() {
        return service.getAllHistories();
    }

    @GetMapping("/{stock_id}")
    public Optional<History> getHistoryByStockId(@PathVariable Long stock_id) {
        List<History> history = service.getHistory(stock_id);
        return history.isEmpty() ? Optional.empty() : Optional.of(history.get(0));
    }


    
}
