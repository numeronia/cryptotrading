package main.java.com.cryptotrade.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;


import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class TradingService {
    
    private final WalletService walletService;
    private final TransactionRepository transactionRepository;
    private final RedissonClient redissonClient;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public TradeService(RedissonClient redissonClient, WalletService walletService, TransactionRepository transactionRepository, TransactionHistoryService transactionHistoryService) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
        this.redissonClient = redissonClient;
        this.transactionHistoryService = transactionHistoryService;
    }

    @Transactional
    public void executeTrade(Long walletId, TradeRequest tradeRequest) {
        // Construct a unique key for the lock
        String lockKey = "walletLock:" + walletId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // Try to acquire the lock with a wait time. The lease time is the time after which the lock will be automatically released.
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // Check if the user has enough balance
                    if (wallet.getBalance() < amountNeeded) {
                        throw new IllegalStateException("Insufficient balance for wallet");
                    } else {

                        // Execute the trade

                        // Update the user's balance
                        wallet.setBalance(wallet.getBalance() - amountNeeded);


                        // Save the trade and log transaction records for user
                        walletRepository.save(wallet);
                        transactionHistoryService.logTransaction(wallet.getUserId(), lockKey, null, lockKey, null);
                    }
                    
     /* Deduct the amount from the wallet (for a BUY trade)
    wallet.setBalance(wallet.getBalance().subtract(amountNeeded));
    walletRepository.save(wallet);

    // Add the amount to the wallet (for a SELL trade)
    wallet.setBalance(wallet.getBalance().add(amountNeeded));
    walletRepository.save(wallet); */
                    

                    
                } finally {
                    // Always unlock in a finally block
                    lock.unlock();
                }
            } else {
                // Handle the case where the lock could not be acquired
                throw new IllegalStateException("Could not acquire lock for wallet: " + walletId);
            }
        } catch (InterruptedException e) {
            // Handle the case where the locking operation was interrupted
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Locking operation was interrupted for wallet: " + walletId);
        }
    }
}
