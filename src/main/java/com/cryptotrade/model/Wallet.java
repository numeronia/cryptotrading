package com.cryptotrade.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long userId;
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<WalletBalance> balances;
    @Version
    private Long version;

    // Constructors, getters, and setters...
    public Wallet() {
    }

    public Wallet(Long userId, Set<WalletBalance> balances) {
        this.userId = userId;
        this.balances = balances;

    }

    // Getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Set<WalletBalance> getBalances(){
        return balances;
    }

    public void setBalances(Set<WalletBalance> balances){
        this.balances = balances;
    }

    public Long getVersion(){
        return version;
    }

    public void setVersion(Long version){
        this.version = version;
    }

    // toString() method...
    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", balances='" + balances + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
