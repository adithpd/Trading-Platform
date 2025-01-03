package com.adithya.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adithya.trading.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    
}
