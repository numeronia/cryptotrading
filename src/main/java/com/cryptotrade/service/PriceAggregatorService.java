package main.java.com.cryptotrade.service;

import org.springframework.stereotype.Service;

@Service
public class PriceAggregatorService {
    
    public AggregatedPrice aggregateBestPrices(BinancePricingData binanceData, HuobiPricingData huobiData) {
        // Logic to compare and find the best bid and ask
        BigDecimal bestBid;
        BigDecimal bestAsk;
        
        AggregatedPrice aggregatedPrice = new AggregatedPrice();
        // Set properties of aggregatedPrice...
        
        return aggregatedPrice;
    }
}
