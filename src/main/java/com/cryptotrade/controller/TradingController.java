package com.cryptotrade.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.cryptotrade.model.Price;
import com.cryptotrade.model.Wallet;
import com.cryptotrade.model.WalletBalance;
import com.cryptotrade.model.TradeRequest;
import com.cryptotrade.model.TransactionHistory;
import com.cryptotrade.service.WalletService;
import com.cryptotrade.service.TradingService;
import com.cryptotrade.service.TransactionHistoryService;
import com.cryptotrade.service.PriceAggregatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TradingController {

    private final WalletService walletService;
    private final TransactionHistoryService transactionHistoryService;
    private final PriceAggregatorService priceAggregatorService;
    @Autowired
    private TradingService tradingService;

    @Autowired
    public TradingController(WalletService walletService, TransactionHistoryService transactionHistoryService, PriceAggregatorService priceAggregatorService) {
        this.walletService = walletService;
        this.transactionHistoryService = transactionHistoryService;
        this.priceAggregatorService = priceAggregatorService;
    }

    @GetMapping("/price/{currencyPair}")
    public ResponseEntity<?> getLatestBestPrice(@PathVariable String currencyPair) {
        List<Price> prices = priceAggregatorService.getAggregatedPrices();
        if (currencyPair.equalsIgnoreCase("BTCUSDT")) {
            Price price = prices.get(0);
            String str = ("BTCUSDT - Bid: " + price.getBidPrice() + " ,Ask: " + price.getAskPrice());
            return ResponseEntity.ok(str);
        } else {
            Price price = prices.get(1);
            String str = ("ETHUSDT - Bid: " + price.getBidPrice() + " ,Ask: " + price.getAskPrice());
            return ResponseEntity.ok(str);
        }
    }

    @PostMapping("/trade")
    public ResponseEntity<?> executeTrade(@RequestBody TradeRequest tradeRequest) {
        try {
            tradingService.executeTrade(tradeRequest);
            return ResponseEntity.ok("Trade successfully executed");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error executing trade");
        }
}
    @GetMapping("/wallet")
    public ResponseEntity<?> getWalletBalance(@RequestParam String userId) {
        try {
            Long uid = Long.parseLong(userId);
            Wallet wallet = walletService.getWalletByUserId(uid);
            Set<WalletBalance> balances = wallet.getBalances();
            String str = new String();
             for (Iterator<WalletBalance> iterator = balances.iterator(); iterator.hasNext();) {
                WalletBalance walletBalance = iterator.next(); 
                if (walletBalance.getCurrency().equals("USDT")) {
                    str += ("USDT: " + walletBalance.getBalance());
                } else if (walletBalance.getCurrency().equals("ETH")) {
                    str += (" ,ETH: " + walletBalance.getBalance());
                } else if (walletBalance.getCurrency().equals("BTC")) {
                    str += (" ,BTC: " + walletBalance.getBalance());
                }
             }
            return ResponseEntity.ok(str);
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error retrieving wallet balance");
        }
    }

    @GetMapping("/tradingHistory")
    public ResponseEntity<?> getTradingHistory(@RequestParam String userId) {
        try {
            List<TransactionHistory> transactions = transactionHistoryService.getUserTransactionHistory(userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error retrieving user trading history");
        }
    }

}
