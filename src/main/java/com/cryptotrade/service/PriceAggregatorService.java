package main.java.com.cryptotrade.service;

import org.springframework.stereotype.Service;

@Service
public class PriceAggregatorService {
    
    public AggregatedPrice aggregateBestPrices(PricingData binanceData, PricingData huobiData) {
        // Logic to compare and find the best bid and ask
        BigDecimal bestBid = ...;
        BigDecimal bestAsk = ...;
        
        AggregatedPrice aggregatedPrice = new AggregatedPrice();
        // Set properties of aggregatedPrice...
        
        return aggregatedPrice;
    }
}
