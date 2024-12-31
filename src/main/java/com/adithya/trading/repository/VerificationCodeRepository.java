package com.adithya.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    public VerificationCode findByUserId(Long userId);
}
