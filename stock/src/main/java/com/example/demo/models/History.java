package com.example.demo.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.hibernate.annotations.JdbcTypeCode;

import org.hibernate.type.SqlTypes;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@AllArgsConstructor
@NoArgsConstructor
@Data
public class History {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long stockId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Nullable
    private Map<Date, Double> history = new HashMap<>();
    
    private Double minPrice;

    private Double maxPrice;

    private String name;
}
