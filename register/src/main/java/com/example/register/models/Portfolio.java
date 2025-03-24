package com.example.register.models;

import java.util.ArrayList;
import java.util.List;


import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @Nullable
    private List<Share> watchlist;

    @OneToMany
    @Nullable
    private List<Share> holdings;

    

    public Portfolio(){
        this.watchlist = new ArrayList<>();
        this.holdings = new ArrayList<>();

    }
}
