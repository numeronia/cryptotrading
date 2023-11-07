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
    public ResponseEntity<Price> getLatestBestPrice(@PathVariable String currencyPair) {
        Price price = tradingService.getLatestBestPrice(currencyPair);
        return ResponseEntity.ok(price);
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
            return ResponseEntity.ok(wallet.getBalances());
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.badRequest().body("Error retrieving wallet balance");
        }
    }

    @GetMapping("/history")
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
