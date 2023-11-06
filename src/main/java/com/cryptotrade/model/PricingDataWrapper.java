package main.java.com.cryptotrade.model;

public class PricingDataWrapper {
    private List<BinancePricingData> binancePricingDataList;
    private List<HuobiPricingData> huobiPricingDataList;

    public PricingDataWrapper() {
    }

    public PricingDataWrapper(List<BinancePricingData> binancePricingDataList, List<HuobiPricingData> huobiPricingDataList) {
        this.binancePricingDataList = binancePricingDataList;
        this.huobiPricingDataList = huobiPricingDataList;
    }

    public List<BinancePricingData> getBinancePricingDataList() {
        return binancePricingDataList;
    }

    public List<HuobiPricingData> getHuobiPricingDataList() {
        return huobiPricingDataList;
    }

    public void setBinancePricingDataList(List<BinancePricingData> binancePricingDataList) {
        this.binancePricingDataList = binancePricingDataList;
    }

    public void setHuobiPricingDataList(List<HuobiPricingData> huobiPricingDataList) {
        this.huobiPricingDataList = huobiPricingDataList;
    }
}
