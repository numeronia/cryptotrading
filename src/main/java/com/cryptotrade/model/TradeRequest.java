package com.cryptotrade.model;

import java.math.BigDecimal;
import com.cryptotrade.model.TradeAction;

public class TradeRequest {
    private String userId; // Or some form of user identification, like username or email
    private String baseCurrency; // Which currency is being used to trade into another currency (USDT -> BTC, USDT -> ETH)
    private String tradedIntoCurrency; // The currency traded into (BTC -> USDT, ETH -> USDT)
    private BigDecimal amount; // The amount of currency to trade
    private TradeAction action; // Enum for BUY or SELL action

    // Constructor
    public TradeRequest(String userId, String baseCurrency, String tradedIntoCurrency, BigDecimal amount, TradeAction action) {
        this.userId = userId;
        this.baseCurrency = baseCurrency;
        this.tradedIntoCurrency = tradedIntoCurrency;
        this.amount = amount;
        this.action = action;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTradedIntoCurrency() {
        return tradedIntoCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TradeAction getAction() {
        return action;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setTradedIntoCurrency(String tradedIntoCurrency) {
        this.tradedIntoCurrency = tradedIntoCurrency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAction(TradeAction action) {
        this.action = action;
    }

    // ToString method for logging or debugging purposes
    @Override
    public String toString() {
        return "TradeRequest{" +
                "userId='" + userId + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", quoteCurrency='" + tradedIntoCurrency + '\'' +
                ", amount=" + amount +
                ", action=" + action +
                '}';
    }
}

