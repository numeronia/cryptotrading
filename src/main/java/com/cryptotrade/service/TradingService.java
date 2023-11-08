package main.java.com.cryptotrade.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.com.cryptotrade.model.Wallet;
import main.java.com.cryptotrade.model.WalletBalance;
import main.java.com.cryptotrade.model.Price;
import main.java.com.cryptotrade.model.TransactionHistory;
import main.java.com.cryptotrade.model.TradeRequest;
import main.java.com.cryptotrade.repository.TransactionHistoryRepository;
import main.java.com.cryptotrade.repository.WalletRepository;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;

import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class TradingService {
    
    private final WalletService walletService;
    private final TransactionHistoryRepository transactionRepository;
    private final RedissonClient redissonClient;
    private final PriceAggregatorService priceAggregatorService;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public TradingService(RedissonClient redissonClient, WalletService walletService, PriceAggregatorService priceAggregatorService, TransactionHistoryRepository transactionRepository, TransactionHistoryService transactionHistoryService) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
        this.priceAggregatorService = priceAggregatorService;
        this.redissonClient = redissonClient;
        this.transactionHistoryService = transactionHistoryService;
    }

    @Transactional
    public void executeTrade(Long walletId, TradeRequest tradeRequest) {
        // Construct a unique key for the lock
        String lockKey = "walletLock:" + walletId;
        RLock lock = redissonClient.getLock(lockKey);
        Wallet wallet = walletService.getWalletByUserId(walletId);
        Set<WalletBalance> walletBalances = wallet.getBalances();
        List<Price> aggregatedPrices = priceAggregatorService.getAggregatedPrices();
        String action = tradeRequest.getAction().getResponse();
        

        try {
            // Try to acquire the lock with a wait time. The lease time is the time after which the lock will be automatically released.
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // Get the currency of the trade
                    String baseCurrency = tradeRequest.getBaseCurrency().getResponse();
                    String tradedIntoCurrency = tradeRequest.getTradedIntoCurrency().getResponse();
                    BigDecimal amount = tradeRequest.getAmount().getResponse();
                    BigDecimal baseCurrencyBalance = BigDecimal.ZERO;
                    BigDecimal tradedCurrencyBalance = BigDecimal.ZERO;

                    for (Iterator<WalletBalance> iterator = walletBalances.iterator(); iterator.hasNext();) {
                        WalletBalance walletBalance = iterator.next();
                        if (walletBalance.getCurrency().equals(baseCurrency)) {
                            baseCurrencyBalance = walletBalance.getBalance();
                        }
                        if (walletBalance.getCurrency().equals(tradedIntoCurrency)) {
                            tradedCurrencyBalance = walletBalance.getBalance();
                        }
                    }

                    // Check if the user has enough balance
                    if (baseCurrencyBalance().compareTo(amount) < 0) {
                        throw new IllegalStateException("Insufficient balance for wallet");
                    } else {
                        Price btcPrice = aggregatedPrices.get(0);
                        Price ethPrice = aggregatedPrices.get(1);

                        // Check if the user is buying or selling
                        if (action.equalsIgnoreCase("BUY")) {
                            // Check if the user is buying BTC or ETH
                            if (tradedIntoCurrency.equalsIgnoreCase("BTC")) {
                                    // Check if the user has enough USDT to buy BTC
                                    if (baseCurrencyBalance.compareTo(amount * btcPrice.getAsk()) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount / btcPrice.getAsk()), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                        // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(wallet.getUserId(), lockKey, baseCurrency, lockKey, amount);
                                    }
                                } else {
                                    // Check if the user has enough USDT to buy ETH
                                    if (baseCurrencyBalance.compareTo(amount * ethPrice.getAsk()) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount / ethPrice.getAsk()), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                        // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(wallet.getUserId(), lockKey, baseCurrency, lockKey, amount);
                                    }
                                }
                            } else { //SELLING BTC/ETH FOR USDT
                                if (baseCurrency.equalsIgnoreCase("BTC")) {
                                    // Check if the user has enough BTC to buy USDT
                                    if (baseCurrencyBalance.compareTo(amount * btcPrice.getBid()) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount * btcPrice.getBid()), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                        // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(wallet.getUserId(), lockKey, baseCurrency, lockKey, amount);
                                    }
                                } else {
                                     if (baseCurrencyBalance.compareTo (amount * ethPrice.getBid()) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount * ethPrice.getBid()), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                    // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(wallet.getUserId(), lockKey, baseCurrency, lockKey, amount);
                                    }
                                }  
                            }             
                        }
                    } 
                    finally {
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
