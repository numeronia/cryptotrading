package com.cryptotrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import com.cryptotrade.model.Wallet;
import com.cryptotrade.model.WalletBalance;
import com.cryptotrade.repository.WalletRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    // Fetch wallet by user ID
    public Wallet getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);

        return wallet;
    }

        // Fetch wallet by wallet ID
    public Wallet getWalletByWalletId(Long walletId) {
        Wallet wallet = walletRepository.findByWalletId(walletId);
 
        return wallet;
    }

    // Update wallet balance for a specific currency
    @Transactional
    public void updateWalletBalance(Long userId, String currencyCode, BigDecimal amount, boolean isCredit) {
        Wallet wallet = getWalletByUserId(userId);
        
        WalletBalance walletBalance = wallet.getBalances().stream()
                .filter(b -> b.getCurrency().equals(currencyCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency not found for wallet: " + userId));
        
        BigDecimal newBalance = isCredit ? walletBalance.getBalance().add(amount) : walletBalance.getBalance().subtract(amount);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient funds for currency: " + currencyCode);
        }
        
        walletBalance.setBalance(newBalance);
        // No need to call save on walletBalance because the changes are cascaded from the Wallet entity.
    } 
}
