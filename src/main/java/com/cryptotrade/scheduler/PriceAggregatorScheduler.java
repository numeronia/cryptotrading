package main.java.com.cryptotrade.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import main.java.com.cryptotrade.model.PricingDataWrapper;
import main.java.com.cryptotrade.repository.PriceRepository;
import main.java.com.cryptotrade.service.PriceAggregatorService;

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
         PricingDataWrapper wrapper = new PricingDataWrapper();

        // Fetch and process the pricing information from Binance
        wrapper = restTemplate.getForObject(BINANCE_API_URL, PricingDataWrapper.class);
        List<BinancePricingData> binancePricingList = wrapper.getBinancePricingDataList();
        // Assuming PricingData is a class that corresponds to the JSON structure of the Binance API response

        // Fetch and process the pricing information from Huobi
        wrapper = restTemplate.getForObject(HUOBI_API_URL, PricingDataWrapper.class);
        List<BinancePricingData> huobiPricingList = wrapper.getHuobiPricingDataList();
        // Similarly, assuming PricingData corresponds to the Huobi API response

        AggregatedPrice bestPrices = priceAggregatorService.aggregateBestPrices(binancePricing, huobiPricing);

        // Save the best price data to the database based on current timestamp
        bestPrices.setTimestamp(new Timestamp(System.currentTimeMillis()));
        priceRepository.save(bestPrices);

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
        }
    }
}

