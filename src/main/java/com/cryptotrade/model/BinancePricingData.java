package main.java.com.cryptotrade.model;

import java.math.BigDecimal;

public class BinancePricingData {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private BigDecimal askPrice;
    private BigDecimal askQty;

    public BinancePricingData() {
    }

    public BinancePricingData(String symbol, BigDecimal bidPrice, BigDecimal bidQty, BigDecimal askPrice, BigDecimal askQty) {
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.bidQty = bidQty;
        this.askPrice = askPrice;
        this.askQty = askQty;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getBidQty() {
        return bidQty;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public BigDecimal getAskQty() {
        return askQty;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setBidQty(BigDecimal bidQty) {
        this.bidQty = bidQty;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public void setAskQty(BigDecimal askQty) {
        this.askQty = askQty;
    }

    @Override
    public String toString() {
        return "BinancePricingData{" +
                "symbol='" + symbol + '\'' +
                ", bidPrice=" + bidPrice +
                ", bidQty=" + bidQty +
                ", askPrice=" + askPrice +
                ", askQty=" + askQty +
                '}';
    }
}
