package com.example.history.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.history.models.History;
import java.util.List;


public interface HistoryRepo extends JpaRepository<History, Integer>{
    List<History> findByStockId(Long stockId);

}
