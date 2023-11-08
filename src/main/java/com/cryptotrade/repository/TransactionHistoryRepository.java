package com.cryptotrade.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import com.cryptotrade.model.TransactionHistory;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, BigDecimal> {
    List<TransactionHistory> findByUserIdOrderByTransactionTimeDesc(String userId);
}