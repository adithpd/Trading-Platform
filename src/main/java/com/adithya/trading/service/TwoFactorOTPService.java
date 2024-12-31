package com.adithya.trading.service;

import com.adithya.trading.model.TwoFactorOTP;
import com.adithya.trading.model.User;

public interface TwoFactorOTPService {
    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt);
    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);
    Boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);
    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
