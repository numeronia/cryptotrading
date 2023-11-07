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
                bestBTCBid = binanceData(i).getBid();
                bestBTCAsk = binanceData(i).getAsk();
            } else if (temp.getSymbol().equalsIgnoreCase("ETHUSDT")) {
                bestETHBid = binanceData(i).getBid();
                bestETHAsk = binanceData(i).getAsk();
            } else {
                continue;
            }
       }

       for (int j = 0; j < huobiData.length(); j++) {
        HuobiPricingData temp = huobiData.get(i);
        if (temp.getSymbol().equalsIgnoreCase("BTCUSDT")) {
            if (temp.getBid() > bestBTCBid) {
                bestBTCBid = huobiData(i).getBid();
            }

            if (temp.getAsk() < bestBTCAsk) {
                bestBTCAsk = huobiData(i).getAsk();
            }

        } else if (temp.getSymbol().equalsIgnoreCase("ETHUSDT")) {
            if (temp.getBid() > bestETHBid) {
                bestETHBid = huobiData(i).getBid();
            }

            if (temp.getAsk() < bestETHAsk) {
                bestETHAsk = huobiData(i).getAsk();
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
