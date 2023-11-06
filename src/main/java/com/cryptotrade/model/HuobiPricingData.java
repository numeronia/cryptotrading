package main.java.com.cryptotrade.model;

public class HuobiPricingData {
    private String symbol;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal close;
    private BigDecimal amount;
    private BigDecimal vol;
    private int count;
    private BigDecimal bid;
    private BigDecimal bidSize;
    private BigDecimal ask;
    private BigDecimal askSize;

    public HuobiPricingData() {
    }

    // Getters and setters...
    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getBidSize() {
        return bidSize;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getAskSize() {
        return askSize;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public void setBidSize(BigDecimal bidSize) {
        this.bidSize = bidSize;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public void setAskSize(BigDecimal askSize) {
        this.askSize = askSize;
    }
}
