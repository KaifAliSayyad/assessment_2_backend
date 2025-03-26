package com.example.demo.models;

import java.util.Date;

import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class History {

    private Integer id;
    private Long stockId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Map<Date, Double> history;
}
