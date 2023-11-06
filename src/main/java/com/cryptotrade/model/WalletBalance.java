package main.java.com.cryptotrade.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class WalletBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    // constructors, getters, and setters...
    public WalletBalance() {
    }

    public WalletBalance(Wallet wallet, String currency, BigDecimal balance) {
        this.wallet = wallet;
        this.currency = currency;
        this.balance = balance;
    }

    // Getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getWallet(){
        return wallet;
    }

    public void setWallet(Wallet wallet){
        this.wallet = wallet;
    }

    public String getCurrency(){
        return currency;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    public void addBalance(BigDecimal balance){
        this.balance = this.balance.add(balance);
    }

    public void subtractBalance(BigDecimal balance){
        this.balance = this.balance.subtract(balance);
    }

    // toString() method...
    @Override
    public String toString() {
        return "WalletBalance{" +
                "id=" + id +
                ", wallet='" + wallet + '\'' +
                ", currency='" + currency + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}

