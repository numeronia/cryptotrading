package main.java.com.cryptotrade.model;

import java.util.List;

public class HuobiWrapper {
    private List<HuobiPricingData> huobiPricingDataList;

    public HuobiWrapper() {
    }

    public HuobiWrapper(List<HuobiPricingData> huobiPricingDataList) {
        this.huobiPricingDataList = huobiPricingDataList;
    }

    public List<HuobiPricingData> getHuobiPricingDataList() {
        return huobiPricingDataList;
    }

    public void setHuobiPricingDataList(List<HuobiPricingData> huobiPricingDataList) {
        this.huobiPricingDataList = huobiPricingDataList;
    }
}
