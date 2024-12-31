package com.adithya.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.TwoFactorOTP;

public interface TwoFactorOTPRepository extends JpaRepository<TwoFactorOTP, String> {
    TwoFactorOTP findByUserId(Long userId);
}
