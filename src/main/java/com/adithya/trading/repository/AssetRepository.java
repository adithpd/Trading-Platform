package com.adithya.trading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.Asset;

public interface AssetRepository extends JpaRepository<Asset,Long> {
    List<Asset> findByUserId(Long user);
    Asset findByUserIdAndCoinId(Long userId, String coinId);
    
}
