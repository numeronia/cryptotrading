package main.java.com.cryptotrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    @Transactional
    public TransactionHistory logTransaction(String userId, String currency, BigDecimal amount, String transactionType, BigDecimal price) {
        TransactionHistory transaction = new TransactionHistory();
        transaction.setUserId(userId);
        transaction.setCurrency(currency);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setPrice(price);
        transaction.setTransactionTime(LocalDateTime.now());
        return transactionHistoryRepository.save(transaction);
    }

    public List<TransactionHistory> getUserTransactionHistory(String userId) {
        return transactionHistoryRepository.findByUserIdOrderByTransactionTimeDesc(userId);
    }
}
