package com.cryptotrade.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cryptotrade.model.Wallet;
import com.cryptotrade.model.WalletBalance;
import com.cryptotrade.model.Price;
import com.cryptotrade.model.TransactionHistory;
import com.cryptotrade.model.TradeRequest;
import com.cryptotrade.repository.TransactionHistoryRepository;
import com.cryptotrade.repository.WalletRepository;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import java.util.Iterator;

import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class TradingService {

    private final WalletService walletService;
    private final WalletRepository walletRepository;
    private final TransactionHistoryRepository transactionRepository;
    private final RedissonClient redissonClient;
    private final PriceAggregatorService priceAggregatorService;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public TradingService(RedissonClient redissonClient, WalletService walletService, WalletRepository walletRepository, PriceAggregatorService priceAggregatorService, TransactionHistoryRepository transactionRepository, TransactionHistoryService transactionHistoryService) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.priceAggregatorService = priceAggregatorService;
        this.redissonClient = redissonClient;
        this.transactionHistoryService = transactionHistoryService;
    }

    @Transactional
    public void executeTrade(TradeRequest tradeRequest) {
        Long userId = Long.parseLong(tradeRequest.getUserId());
        Wallet wallet = walletService.getWalletByWalletId(userId);
        // Construct a unique key for the lock
        String lockKey = "walletLock:" + wallet.getId();
        RLock lock = redissonClient.getLock(lockKey);
        Set<WalletBalance> walletBalances = wallet.getBalances();
        List<Price> aggregatedPrices = priceAggregatorService.getAggregatedPrices();
        String action = tradeRequest.getAction().getResponse();
        

        try {
            // Try to acquire the lock with a wait time. The lease time is the time after which the lock will be automatically released.
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // Get the currency of the trade
                    String baseCurrency = tradeRequest.getBaseCurrency();
                    String tradedIntoCurrency = tradeRequest.getTradedIntoCurrency();
                    BigDecimal amount = tradeRequest.getAmount();
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
                    if (baseCurrencyBalance.compareTo(amount) < 0) {
                        throw new IllegalStateException("Insufficient balance for wallet");
                    } else {
                        Price btcPrice = aggregatedPrices.get(0);
                        Price ethPrice = aggregatedPrices.get(1);

                        // Check if the user is buying or selling
                        if (action.equalsIgnoreCase("BUY")) {
                            // Check if the user is buying BTC or ETH
                            if (tradedIntoCurrency.equalsIgnoreCase("BTC")) {
                                    // Check if the user has enough USDT to buy BTC
                                    if (baseCurrencyBalance.compareTo(amount.multiply(btcPrice.getAskPrice())) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount.divide(btcPrice.getAskPrice())), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                        // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(tradeRequest.getUserId(), baseCurrency, amount, tradeRequest.getAction().toString(), (amount.divide(btcPrice.getAskPrice())));
                                    }
                                } else {
                                    // Check if the user has enough USDT to buy ETH
                                    if (baseCurrencyBalance.compareTo(amount.multiply(ethPrice.getAskPrice())) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount.divide(ethPrice.getAskPrice())), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                        // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(tradeRequest.getUserId(), baseCurrency, amount, tradeRequest.getAction().toString(), (amount.divide(ethPrice.getAskPrice())));
                                    }
                                }
                            } else { //SELLING BTC/ETH FOR USDT
                                if (baseCurrency.equalsIgnoreCase("BTC")) {
                                    // Check if the user has enough BTC to buy USDT
                                    if (baseCurrencyBalance.compareTo(amount.multiply(btcPrice.getBidPrice())) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount.multiply(btcPrice.getBidPrice())), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                        // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(tradeRequest.getUserId(), baseCurrency, amount, tradeRequest.getAction().toString(), (amount.multiply(btcPrice.getBidPrice())));
                                    }
                                } else {
                                     if (baseCurrencyBalance.compareTo (amount.multiply(ethPrice.getBidPrice())) < 0) {
                                        throw new IllegalStateException("Insufficient balance for wallet");
                                    } else {
                                        // Update the user's balance
                                        walletService.updateWalletBalance(wallet.getUserId(), tradedIntoCurrency, (amount.multiply(ethPrice.getBidPrice())), true);
                                        walletService.updateWalletBalance(wallet.getUserId(), baseCurrency, amount, false);

                                    // Increment wallet version, save the changed balance and log trade transaction records for user
                                        wallet.setVersion(wallet.getVersion() + 1);
                                        walletRepository.save(wallet);
                                        transactionHistoryService.logTransaction(tradeRequest.getUserId(), baseCurrency, amount, tradeRequest.getAction().toString(), (amount.multiply(ethPrice.getBidPrice())));
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
                throw new IllegalStateException("Could not acquire lock for wallet");
            }
        } catch (InterruptedException e) {
            // Handle the case where the locking operation was interrupted
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Locking operation was interrupted for wallet");
        }
    }
}
