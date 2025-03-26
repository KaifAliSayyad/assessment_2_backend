package com.example.history.models;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class HistoryDTO {
    private Long stockId;
    private Date date;
    private Double price;

    public History toHistory(History prevHistory) {
        Map<Date, Double> history = prevHistory.getHistory();
        if (history == null) {
            history = new HashMap<>();
        }
        history.put(date, price);
        prevHistory.setHistory(history);
        return prevHistory;
    }
}
