package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer userId;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Stocks> watchlist = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserStock> holdings = new ArrayList<>();
    
    private Double value = 0.0;
}
