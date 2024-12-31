package com.adithya.trading.model;

import com.adithya.trading.domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
