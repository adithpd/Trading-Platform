package com.adithya.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, String> {
    
}
