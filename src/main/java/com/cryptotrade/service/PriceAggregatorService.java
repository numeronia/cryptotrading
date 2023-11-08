package main.java.com.cryptotrade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import main.java.com.cryptotrade.model.BinancePricingData;
import main.java.com.cryptotrade.model.HuobiPricingData;
import main.java.com.cryptotrade.model.Price;

@Service
public class PriceAggregatorService {
    
    public List<Price> aggregateBestPrices(List<BinancePricingData> binanceData, List<HuobiPricingData> huobiData) {
        // Logic to compare and find the best bid and ask
        BigDecimal bestBTCBid;
        BigDecimal bestBTCAsk;
        BigDecimal bestETHBid;
        BigDecimal bestETHAsk;

        // Logic to aggregate the best bid and ask
        // General understanding: buying low and selling high - thus, the best price is one where the buy in is lower than the other, and sell is higher than the other.
        for (int i = 0; i < binanceData.length(); i++) {
            BinancePricingData temp = binanceData.get(i);
            if (temp.getSymbol().equalsIgnoreCase("BTCUSDT")) {
                bestBTCBid = temp.getBidPrice();
                bestBTCAsk = temp.getAskPrice();
            } else if (temp.getSymbol().equalsIgnoreCase("ETHUSDT")) {
                bestETHBid = temp.getBidPrice();
                bestETHAsk = temp.getAskPrice();
            } else {
                continue;
            }
       }

       for (int j = 0; j < huobiData.length(); j++) {
        HuobiPricingData temp = huobiData.get(i);
        if (temp.getSymbol().equalsIgnoreCase("BTCUSDT")) {
            if (temp.getBid().compareTo(bestBTCBid) > 0) {
                bestBTCBid = temp.getBid();
            }

            if (temp.getAsk().compareTo(bestBTCAsk) < 0) {
                bestBTCAsk = temp.getAsk();
            }

        } else if (temp.getSymbol().equalsIgnoreCase("ETHUSDT")) {
            if (temp.getBid().compareTo(bestETHBid) > 0) {
                bestETHBid = temp.getBid();
            }

            if (temp.getAsk().compareTo(bestETHAsk) < 0) {
                bestETHAsk = temp.getAsk();
            }

        } else {
            continue;
        }
       }
       
       // Create list of best prices, instantiate aggregated currencies in Price object and return the list.
        List<Price> priceList = new ArrayList();
        Price aggregatedBTCUSDTPrice = new Price("BTCUSDT", bestBTCBid, bestBTCAsk);
        Price aggregatedETHUSDTPrice = new Price("ETHUSDT", bestETHBid, bestETHAsk);
        priceList.add(aggregatedBTCUSDTPrice);
        priceList.add(aggregatedETHUSDTPrice);
        
        return priceList;
    }

    public List<Price> getAggregatedPrices() {
        Price btcPrice = priceRepository.findBySymbol("BTCUSDT");
        Price ethPrice = priceRepository.findBySymbol("ETHUSDT");
        List<Price> priceList = new ArrayList();
        priceList.add(btcPrice);
        priceList.add(ethPrice);
        return priceList;
   }
}
