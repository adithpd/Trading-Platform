package com.adithya.trading.service;

import com.adithya.trading.model.Order;
import com.adithya.trading.model.User;
import com.adithya.trading.model.Wallet;

public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user);
}
