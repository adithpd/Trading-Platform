package com.adithya.trading.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.adithya.trading.domain.VerificationType;
import com.adithya.trading.model.User;
import com.adithya.trading.model.VerificationCode;
import com.adithya.trading.repository.VerificationCodeRepository;
import com.adithya.trading.utils.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode.isPresent()) {
            return verificationCode.get();
        }

        throw new Exception("Verification Code Not Found");
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOTP());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);
        return verificationCodeRepository.save(verificationCode1);
    }
    
}
