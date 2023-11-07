package main.java.com.cryptotrade.controller;

import java.util.List;

import main.java.com.cryptotrade.model.Price;
import main.java.com.cryptotrade.model.Wallet;
import main.java.com.cryptotrade.service.TradingService;

@RestController
@RequestMapping("/api")
public class TradingController {

    private final PriceService priceService;
    private final WalletService walletService;
    private final TransactionHistoryService transactionHistoryService;
    @Autowired
    private TradingService tradingService;

    @GetMapping("/price/{currencyPair}")
    public ResponseEntity<?> getLatestBestPrice(@PathVariable String currencyPair) {
        List<Price> prices = tradingService.getAggregatedPrices(currencyPair);
        if (currencyPair.equalsIgnoreCase("BTCUSDT")) {
            Price price = prices.get(0);
            String str = ("Bid: " + price.getBidPrice() + " ,Ask: " + price.getAskPrice());
            return ResponseEntity.ok(str);
        } else {
            Price price = prices.get(1);
            String str = ("Bid: " + price.getBidPrice() + " ,Ask: " + price.getAskPrice());
            return ResponseEntity.ok(str);
        }
    }

    @PostMapping("/trade")
    public ResponseEntity<Trade> executeTrade(@RequestBody TradeRequest tradeRequest) {
        try {
            TradeResult tradeResult = tradingService.executeTrade(tradeRequest);
            return ResponseEntity.ok(tradeResult);
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error executing trade");
        }
}
    @GetMapping("/wallet")
    public ResponseEntity<?> getWalletBalance(@RequestParam String userId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            List<WalletBalance> balances = wallet.getBalances();
            String str = ("USDT: " + balances.get(0).getBalance() + " ,BTC: " + balances.get(1).getBalance() + " ,ETH: " + balances.get(2).getBalance());
            return ResponseEntity.ok(str);
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error retrieving wallet balance");
        }
    }

    @GetMapping("/tradingHistory")
    public ResponseEntity<?> getTradingHistory(@RequestParam String userId) {
        try {
            List<Transaction> transactions = transactionHistoryService.getTransactionsForUser(userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error retrieving user trading history");
        }
    }

}
