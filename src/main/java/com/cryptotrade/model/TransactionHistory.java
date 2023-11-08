package com.cryptotrade.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionHistory {

    private String userId;
    private String currency;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal price;
    private LocalDateTime transactionTime;

    public TransactionHistory() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrency(){
        return currency;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public String getTransactionType(){
        return transactionType;
    }

    public void setTransactionType(String transactionType){
        this.transactionType = transactionType;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public LocalDateTime getTransactionTime(){
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime){
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return "TransactionHistory{" +
                "userId='" + userId + '\'' +
                ", currency='" + currency + '\'' +
                ", amount='" + amount + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", price='" + price + '\'' +
                ", transactionTime='" + transactionTime + '\'' +
                '}';
    }

}
