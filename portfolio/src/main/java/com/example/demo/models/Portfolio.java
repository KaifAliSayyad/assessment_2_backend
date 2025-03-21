package com.example.demo.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;

@Entity
@ToString
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Stocks> getWatchlist() {
		return watchlist;
	}

	public void setWatchlist(List<Stocks> watchlist) {
		this.watchlist = watchlist;
	}

	public List<UserStock> getHoldings() {
		return holdings;
	}

	public void setHoldings(List<UserStock> holdings) {
		this.holdings = holdings;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Portfolio [id=" + id + ", userId=" + userId + ", watchlist=" + watchlist + ", holdings=" + holdings
				+ ", value=" + value + "]";
	}
    
    
}
