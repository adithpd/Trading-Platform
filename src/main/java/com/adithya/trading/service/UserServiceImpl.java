package com.adithya.trading.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.adithya.trading.config.JwtProvider;
import com.adithya.trading.domain.VerificationType;
import com.adithya.trading.model.TwoFactorAuth;
import com.adithya.trading.model.User;
import com.adithya.trading.repository.UserRepository;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user==null) {
            throw new Exception("User Not Found");
        }
        
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new Exception("User Not Found");
        }

        return user.get();
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user==null) {
            throw new Exception("User Not Found");
        }

        return user;
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
    
}
