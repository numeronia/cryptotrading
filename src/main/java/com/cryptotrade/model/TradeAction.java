package com.cryptotrade.model;

public enum TradeAction {
    BUY("BUY"), SELL("SELL");
    private String value;

    private TradeAction(String value) {
        this.value = value;
    }

    public String getResponse() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}