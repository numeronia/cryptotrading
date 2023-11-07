package main.java.com.cryptotrade.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.com.cryptotrade.model.Wallet;

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
    private final PriceAggregatorService priceAggregatorService;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public TradeService(RedissonClient redissonClient, WalletService walletService, PriceAggregatorService priceAggregatorService, TransactionRepository transactionRepository, TransactionHistoryService transactionHistoryService) {
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
        Wallet wallet = walletService.getWalletById(walletId);
        Set<WalletBalance> walletBalance = wallet.getBalances();
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
                    BigDecimal baseCurrencyBalance = walletBalance.get(baseCurrency).getBalance();
                    BigDecimal tradedCurrencyBalance = walletBalance.get(tradedIntoCurrency).getBalance();

                    // Check if the user has enough balance
                    if (currentBalance() < amountNeeded) {
                        throw new IllegalStateException("Insufficient balance for wallet");
                    } else {
                        Price btcPrice = aggregatedPrices.get(0);
                        Price ethPrice = aggregatedPrices.get(1);

                        // Check if the user is buying or selling
                        if (action.equalsIgnoreCase("BUY")) {
                            // Check if the user is buying BTC or ETH
                            if (tradedIntoCurrency.equalsIgnoreCase("BTC")) {
                                    // Check if the user has enough USDT to buy BTC
                                    if (baseCurrencyBalance < (amount * btcPrice.getAsk())) {
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
                                    if (baseCurrencyBalance < (amount * ethPrice.getAsk())) {
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
                                    if (baseCurrencyBalance < (amount * btcPrice.getBid())) {
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
                                     if (baseCurrencyBalance < (amount * ethPrice.getBid())) {
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
