package com.adithya.trading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findByUserId(Long userId);

    Optional<Wallet> findById(Long id);
}
