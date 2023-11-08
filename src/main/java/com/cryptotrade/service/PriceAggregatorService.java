package com.cryptotrade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cryptotrade.model.BinancePricingData;
import com.cryptotrade.model.HuobiPricingData;
import com.cryptotrade.model.Price;
import com.cryptotrade.repository.PriceRepository;


@Service
public class PriceAggregatorService {

    private final PriceRepository priceRepository;

    @Autowired
    PriceAggregatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }
    
    public List<Price> aggregateBestPrices(List<BinancePricingData> binanceData, List<HuobiPricingData> huobiData) {
        // Logic to compare and find the best bid and ask
        BigDecimal bestBTCBid = BigDecimal.ZERO;
        BigDecimal bestBTCAsk = BigDecimal.ZERO;
        BigDecimal bestETHBid = BigDecimal.ZERO;
        BigDecimal bestETHAsk = BigDecimal.ZERO;

        // Logic to aggregate the best bid and ask
        // General understanding: buying low and selling high - thus, the best price is one where the buy in is lower than the other, and sell is higher than the other.
        for (int i = 0; i < binanceData.size(); i++) {
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

       for (int j = 0; j < huobiData.size(); j++) {
        HuobiPricingData temp = huobiData.get(j);
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
