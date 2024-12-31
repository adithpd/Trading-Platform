package com.adithya.trading.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adithya.trading.domain.VerificationType;
import com.adithya.trading.model.ForgotPasswordToken;
import com.adithya.trading.model.User;
import com.adithya.trading.repository.ForgotPasswordRepository;

@Service
public class ForgotPasswordImpl implements ForgotPasswordService {
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    
}
