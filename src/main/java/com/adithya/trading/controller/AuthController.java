package com.adithya.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adithya.trading.config.JwtProvider;
import com.adithya.trading.model.TwoFactorOTP;
import com.adithya.trading.model.User;
import com.adithya.trading.repository.UserRepository;
import com.adithya.trading.response.AuthResponse;
import com.adithya.trading.service.CustomUserDetailsService;
import com.adithya.trading.service.EmailService;
import com.adithya.trading.service.TwoFactorOTPService;
import com.adithya.trading.utils.OtpUtils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")    
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        
        if(isEmailExist != null){
            throw new Exception("Email is already used with another account");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());
        userRepository.save(newUser);


        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register Success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")    
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
        
        String userName = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(userName);

        if(user.getTwoFactorAuth().isEnabled()) {
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor Auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOTP();
            TwoFactorOTP oldTwoFactorOTP  = twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwoFactorOTP!=null) {
                twoFactorOTPService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
            TwoFactorOTP newTwoFactorOTP = twoFactorOTPService.createTwoFactorOTP(authUser, otp, jwt);
            emailService.sendVerificationOtpEmail(userName, otp);
            res.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login Success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        if(userDetails==null) {
            throw new BadCredentialsException("Invalid Username");
        }
        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOTP(@PathVariable String otp, @RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor Authentication Verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("invalid otp");
    }
}
