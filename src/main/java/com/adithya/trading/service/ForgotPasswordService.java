package com.adithya.trading.service;

import com.adithya.trading.domain.VerificationType;
import com.adithya.trading.model.ForgotPasswordToken;
import com.adithya.trading.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo);
    ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUser(Long userId);
    void deleteToken(ForgotPasswordToken token);
}
