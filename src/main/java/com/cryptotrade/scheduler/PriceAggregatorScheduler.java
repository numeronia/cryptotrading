package main.java.com.cryptotrade.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import main.java.com.cryptotrade.repository.PriceRepository;
import main.java.com.cryptotrade.service.PriceAggregatorService;
import main.java.com.cryptotrade.model.Price;
import main.java.com.cryptotrade.model.BinanceWrapper;
import main.java.com.cryptotrade.model.BinancePricingData;
import main.java.com.cryptotrade.model.HuobiWrapper;
import main.java.com.cryptotrade.model.HuobiPricingData;
import java.sql.Timestamp;

@Component
public class PriceAggregatorScheduler {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PriceRepository priceRepository; // An interface to handle database operations for the AggregatedPrices table

     @Autowired
    private PriceAggregatorService priceAggregatorService;

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String HUOBI_API_URL = "https://api.huobi.pro/market/tickers";

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void fetchAndStoreBestPrices() {
        try {
         BinanceWrapper binanceWrapper = new BinanceWrapper();
         HuobiWrapper huobiWrapper = new HuobiWrapper();
        // Fetch and process the pricing information from Binance
        binanceWrapper = restTemplate.getForObject(BINANCE_API_URL, BinanceWrapper.class);
        List<BinancePricingData> binancePricingList = binanceWrapper.getBinancePricingDataList();

        // Fetch and process the pricing information from Huobi
        huobiWrapper = restTemplate.getForObject(HUOBI_API_URL, HuobiWrapper.class);
        List<HuobiPricingData> huobiPricingList = huobiWrapper.getHuobiPricingDataList();

        // Pass the two pricing lists into the aggregator service to aggregate the best prices for both currencies.
        List<Price> bestPrices = priceAggregatorService.aggregateBestPrices(binancePricingList, huobiPricingList);

        // Save the best price data to the database based on current timestamp
        for (int i = 0; i < bestPrices.size(); i++) {
            Price price = bestPrices.get(i);
            price.setTimestamp(new Timestamp(System.currentTimeMillis()));
            priceRepository.save(price);
        }

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
        }
    }
}

