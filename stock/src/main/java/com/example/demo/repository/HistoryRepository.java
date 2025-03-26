package com.example.demo.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.models.History;
import com.example.demo.models.HistoryDTO;

@FeignClient(name = "history", url = "${history.service.url}")
public interface HistoryRepository {
    @RequestMapping(value="", method=RequestMethod.GET)
    public List<History> getAllHistories();

    @RequestMapping(value="/{stock_id}", method=RequestMethod.GET)
    public History getHistoryByStockId(@PathVariable Long stock_id);

    @RequestMapping(value="", method=RequestMethod.POST, consumes="application/json")
    public String saveHistory(@RequestBody  HistoryDTO historydto);
}
