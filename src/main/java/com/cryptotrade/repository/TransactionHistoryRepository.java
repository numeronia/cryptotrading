package main.java.com.cryptotrade.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.com.cryptotrade.model.Wallet;
import main.java.com.cryptotrade.model.WalletBalance;

import java.math.BigDecimal;
import java.util.Optional;
import main.java.com.cryptotrade.model.TransactionHistory;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, BigDecimal> {
    List<TransactionHistory> findByUserIdOrderByTransactionTimeDesc(String userId);
}