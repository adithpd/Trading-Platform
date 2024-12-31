package com.adithya.trading.service;

import com.adithya.trading.domain.VerificationType;
import com.adithya.trading.model.User;
import com.adithya.trading.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id) throws Exception;
    VerificationCode getVerificationCodeByUser(Long userId);
    void deleteVerificationCodeById(VerificationCode verificationCode);
}