package com.example.history.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.history.models.History;
import com.example.history.models.HistoryDTO;
import com.example.history.repos.HistoryRepo;

@Service
public class HistoryService {
    
    @Autowired
    private HistoryRepo repo;

    public List<History> getAllHistories(){
        return repo.findAll();
    }

    public List<History> getHistory(Long id){
        return repo.findByStockId(id);
    }

    public History saveHistory(HistoryDTO history){
        List<History> prevHistory = repo.findByStockId(history.getStockId());
        if(prevHistory == null || prevHistory.size() == 0 || prevHistory.isEmpty()){
            System.out.println("Creating new history object for stock id: " + history.getStockId());
            History historyObj = new History();
            historyObj.setStockId(history.getStockId());
            History newHistory = history.toHistory(historyObj);
            return repo.save(newHistory);
        }
        History newHistory = history.toHistory(prevHistory.get(0));
        return repo.save(newHistory);
    }
}
