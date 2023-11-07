package main.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import main.java.com.cryptotrade.model.Wallet;
import main.java.com.cryptotrade.model.WalletBalance;
import main.java.com.cryptotrade.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final WalletRepository walletRepository;

    @Autowired
    public DatabaseLoader(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Assuming the user's identifier is set as a constant or retrieved from a secured place
        final String userId = "user-123";

        Wallet wallet = walletRepository.findById(userId).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setUserId(userId);
            newWallet.setBalances(new HashSet<>());
            return newWallet;
        });

        wallet.getBalances().clear();
         WalletBalance walletBalance = new WalletBalance();
         WalletBalance walletBalanceETH = new WalletBalance();
         WalletBalance walletBalanceBTC = new WalletBalance();

            walletBalance.setUserId(userId);
            walletBalance.setWallet(wallet);
            walletBalance.setCurrency("USDT");
            walletBalance.setBalance(new BigDecimal("50000"));
            wallet.getBalances().add(walletBalance);

            walletBalanceETH.setUserId(userId);
            walletBalanceETH.setWallet(wallet);
            walletBalanceETH.setCurrency("ETH");
            walletBalanceETH.setBalance(BigDecimal.ZERO);
            wallet.getBalances().add(walletBalanceETH);

            walletBalanceBTC.setUserId(userId);
            walletBalanceBTC.setWallet(wallet);
            walletBalanceBTC.setCurrency("BTC");
            walletBalanceBTC.setBalance(BigDecimal.ZERO);
            wallet.getBalances().add(walletBalanceBTC);

            // Save to the repository, which saves to the H2 database
            walletRepository.save(wallet);
    
    }
}
