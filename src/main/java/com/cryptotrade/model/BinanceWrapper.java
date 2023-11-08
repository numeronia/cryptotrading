package com.cryptotrade.model;

import java.util.List;

public class BinanceWrapper {
    private List<BinancePricingData> binancePricingDataList;

    public BinanceWrapper() {
    }

    public BinanceWrapper(List<BinancePricingData> binancePricingDataList) {
        this.binancePricingDataList = binancePricingDataList;
    }

    public List<BinancePricingData> getBinancePricingDataList() {
        return binancePricingDataList;
    }

    public void setBinancePricingDataList(List<BinancePricingData> binancePricingDataList) {
        this.binancePricingDataList = binancePricingDataList;
    }
}
