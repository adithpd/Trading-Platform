package com.adithya.trading.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adithya.trading.domain.VerificationType;
import com.adithya.trading.model.ForgotPasswordToken;
import com.adithya.trading.model.User;
import com.adithya.trading.model.VerificationCode;
import com.adithya.trading.request.ForgotPasswordTokenRequest;
import com.adithya.trading.request.ResetPasswordRequest;
import com.adithya.trading.response.ApiResponse;
import com.adithya.trading.response.AuthResponse;
import com.adithya.trading.service.EmailService;
import com.adithya.trading.service.ForgotPasswordService;
import com.adithya.trading.service.UserService;
import com.adithya.trading.service.VerificationCodeService;
import com.adithya.trading.utils.OtpUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOTP(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if(verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("Verification OTP Sent Successfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();
        boolean isVerified = verificationCode.getOtp().equals(otp);

        if(isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong OTP");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOTP(@RequestBody ForgotPasswordTokenRequest req) throws Exception {
        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
        if(token == null) {
            token = forgotPasswordService.createToken(user, id, otp, req.getVerificationType(), req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset OTP sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id, @RequestBody ResetPasswordRequest req, @RequestHeader("Authorization") String jwt) throws Exception {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);
        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());
        if(isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMesssage("Password Update Successfully");
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong OTP");
    }
}
