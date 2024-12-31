package com.adithya.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken,String> {
    ForgotPasswordToken findByUserId(Long userId);
}
