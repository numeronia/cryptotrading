package main.java.com.cryptotrade.model;

import java.math.BigDecimal;

public class TradeRequest {
    private String userId; // Or some form of user identification, like username or email
    private String baseCurrency; // The currency that is being bought or sold (USDT -> BTC, USDT -> ETH)
    private String quoteCurrency; // The currency to quote the trade in (BTC -> USDT, ETH -> USDT)
    private BigDecimal amount; // The amount of currency to trade
    private TradeAction action; // Enum for BUY or SELL action

    // Constructor
    public TradeRequest(String userId, String baseCurrency, String quoteCurrency, BigDecimal amount, TradeAction action) {
        this.userId = userId;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
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

    public String getQuoteCurrency() {
        return quoteCurrency;
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

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
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
                ", quoteCurrency='" + quoteCurrency + '\'' +
                ", amount=" + amount +
                ", action=" + action +
                '}';
    }
}

