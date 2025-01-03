package com.adithya.trading.service;

import com.adithya.trading.model.PaymentDetails;
import com.adithya.trading.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user);
    public PaymentDetails getUserPaymentDetails(User user);
}
