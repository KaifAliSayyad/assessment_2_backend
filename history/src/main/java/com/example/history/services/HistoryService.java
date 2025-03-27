package com.example.history.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.history.models.History;
import com.example.history.models.HistoryDTO;
import com.example.history.repos.HistoryRepo;

@Service
public class HistoryService {
    
    @Autowired
    private HistoryRepo repo;


    public History initializeHistory(History history){
        System.out.println("******************************************Creating new history object " + history+"*************************************");
        return repo.save(history);
    }

    public List<History> getAllHistories(){
        return repo.findAll();
    }

    public List<History> getHistory(Long id){
        return repo.findByStockId(id);
    }

    public History saveHistory(HistoryDTO history){
        List<History> prevHistory = repo.findByStockId(history.getStockId());
        History newHistory = history.toHistory(prevHistory.get(0));
        return repo.save(newHistory);
    }
}
