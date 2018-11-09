package edu.android.itwill.project_oil_hub.Model;

public class Price {
    private String oilType;
    private int oilPrice;
    private String tradeDay;
    private String tradeTime;

    public Price(String oilType,
                 int oilPrice,
                 String tradeDay,
                 String tradeTime) {
        this.oilType = oilType;
        this.oilPrice = oilPrice;
        this.tradeDay = tradeDay;
        this.tradeTime = tradeTime;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public int getOilPrice() {
        return oilPrice;
    }

    public void setOilPrice(int oilPrice) {
        this.oilPrice = oilPrice;
    }

    public String getTradeDay() {
        return tradeDay;
    }

    public void setTradeDay(String tradeDay) {
        this.tradeDay = tradeDay;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Override
    public String toString() {
        return "Price{" +
                "oilType='" + oilType + '\'' +
                ", oilPrice=" + oilPrice +
                ", tradeDay='" + tradeDay + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                '}';
    }
}