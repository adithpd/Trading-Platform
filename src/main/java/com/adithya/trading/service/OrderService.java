package com.adithya.trading.service;

import java.util.List;

import com.adithya.trading.domain.OrderType;
import com.adithya.trading.model.Coin;
import com.adithya.trading.model.Order;
import com.adithya.trading.model.OrderItem;
import com.adithya.trading.model.User;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId) throws Exception;
    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);
    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
