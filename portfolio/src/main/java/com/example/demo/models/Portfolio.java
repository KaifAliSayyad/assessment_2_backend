package com.example.demo.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import io.micrometer.common.lang.Nullable;

import java.util.ArrayList;

@Entity
@ToString
@Getter
@Setter
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer userId;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Stocks> watchlist = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserStock> holdings = new ArrayList<>();
    
	@Nullable
    private Double value = 0.0;
    
}
