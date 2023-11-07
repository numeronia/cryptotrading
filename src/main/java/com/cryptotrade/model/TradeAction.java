package main.java.com.cryptotrade.model;

public enum TradeAction {
    BUY("BUY"), SELL("SELL");
    private String value;

    public String getResponse() {
        return value;
    }
}