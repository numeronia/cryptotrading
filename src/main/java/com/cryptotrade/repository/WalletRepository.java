package main.java.com.cryptotrade.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.com.cryptotrade.model.Wallet;
import main.java.com.cryptotrade.model.WalletBalance;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
// Find a wallet balance by the user's ID and the currency
    Optional<WalletBalance> findByUserIdAndCurrency(String userId, String currency);
    
    // Method to increase the balance of a specific currency in the user's wallet balance
    @Modifying
    @Query("UPDATE WalletBalance wb SET wb.balance = wb.balance + :amount WHERE wb.userId = :userId AND wb.currency = :currency")
    int increaseBalance(String userId, String currency, BigDecimal amount);
    
    // Method to decrease the balance of a specific currency in the user's wallet balance
    @Modifying
    @Query("UPDATE WalletBalance wb SET wb.balance = wb.balance - :amount WHERE wb.userId = :userId AND wb.currency = :currency AND wb.balance >= :amount")
    int decreaseBalance(String userId, String currency, BigDecimal amount);
    
    // Method to check if a wallet balance has enough balance for a specific currency
    boolean existsByUserIdAndCurrencyAndBalanceGreaterThanEqual(String userId, String currency, BigDecimal balance);
}
