package com.adithya.trading.service;

import com.adithya.trading.model.Coin;
import com.adithya.trading.model.User;
import com.adithya.trading.model.Watchlist;

public interface WatchlistService {
    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createWatchlist(User user);
    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}
