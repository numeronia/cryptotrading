package main.java.com.cryptotrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    // Fetch wallet balances by user ID
    public Wallet getWalletByUserId(String userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
    }

    // Update wallet balance for a specific currency
    @Transactional
    public void updateWalletBalance(String userId, String currencyCode, BigDecimal amount, boolean isCredit) {
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
