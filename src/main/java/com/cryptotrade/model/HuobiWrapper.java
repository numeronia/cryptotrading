package main.java.com.cryptotrade.model;

public class HuobiWrapper {
    private List<HuobiPricingData> huobiPricingDataList;

    public HuobiWrapper() {
    }

    public PricingDataWrapper(List<HuobiPricingData> huobiPricingDataList) {
        this.huobiPricingDataList = huobiPricingDataList;
    }

    public List<HuobiPricingData> getHuobiPricingDataList() {
        return huobiPricingDataList;
    }

    public void setHuobiPricingDataList(List<HuobiPricingData> huobiPricingDataList) {
        this.huobiPricingDataList = huobiPricingDataList;
    }
}
